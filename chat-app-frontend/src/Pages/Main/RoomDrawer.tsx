import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Avatar, Drawer, Button, Dropdown, Menu, Modal, Popconfirm, message, Tag } from 'antd';
import { ExclamationCircleFilled, MoreOutlined, PlusOutlined } from '@ant-design/icons';

import { RootState } from '../../store';
import { COLOR_LISTS_LENGTH, randomColor } from '../../utils/random';
import { RoomActionType } from '../../@types/enums';
import { Loading } from '../../Components/Loading';

import './styles.scss';
import request from '../../utils/request';
import { fetchRoomUsers, leaveRoom } from '../../store/actions/room';

interface RoomDrawerProps {
  visible: boolean;
  onClose: () => void;
}

message.config({
  top: 180,
  duration: 2
});

export default function RoomDrawer(props: RoomDrawerProps): JSX.Element {
  const dispatch = useDispatch();
  const user = useSelector((state: RootState) => state.user.user);
  const { currentRoom, blockedUsers, normalUsers, isAdmin, fetchUsersLoading } = useSelector(
    (state: RootState) => state.room
  );

  const [otherUsers, setOtherUsers] = useState<RoomUserType[]>([]);

  const [memberModalVisible, setMemberModalVisible] = useState<boolean>(false);
  const [inviteListLoading, setInviteListLoading] = useState<boolean>(false);

  const blockOrUnblockUser = async (userId: number, toBlock: boolean) => {
    try {
      dispatch({ type: RoomActionType.FETCH_USERS_LOADING });
      await request<string, { user: number; chatRoomId: number }>({
        method: 'POST',
        url: toBlock ? '/blockUser' : '/unblockUser',
        body: {
          user: userId,
          chatRoomId: currentRoom!.chatRoomId
        }
      });
      message.success('Operation succeeded');
      dispatch(fetchRoomUsers(currentRoom!.chatRoomId));
    } catch (error) {
      console.log((error as Error).message);
    }
  };

  const removeUser = async (userId: number) => {
    try {
      await request<string, { user: number; chatRoomId: number }>({
        method: 'POST',
        url: '/removeUser',
        body: {
          user: userId,
          chatRoomId: currentRoom!.chatRoomId
        }
      });
      message.success('Operation succeeded');
      dispatch(fetchRoomUsers(currentRoom!.chatRoomId));
    } catch (error) {
      console.log((error as Error).message);
    }
  };

  const renderMenu = (isBlocked: boolean, user: RoomUserType) => {
    return (
      <Menu key={user.userId}>
        {!isBlocked && (
          <Menu.Item
            key={1}
            onClick={() => {
              dispatch({ type: RoomActionType.SET_PRIVATE_MESSAGE, payload: user });
              props.onClose();
            }}
          >
            Private Message
          </Menu.Item>
        )}
        {isAdmin && (
          <>
            {!isBlocked ? (
              <Menu.Item key={2} onClick={() => blockOrUnblockUser(user.userId, true)}>
                Block
              </Menu.Item>
            ) : (
              <Menu.Item key={3} onClick={() => blockOrUnblockUser(user.userId, false)}>
                Unblock
              </Menu.Item>
            )}
            <Menu.Item key={4} onClick={() => removeUser(user.userId)}>
              Remove from room
            </Menu.Item>
          </>
        )}
      </Menu>
    );
  };

  const showDeleteConfirm = () => {
    Modal.confirm({
      title: 'Are you sure to leave this room?',
      icon: <ExclamationCircleFilled />,
      content: 'This operation cannot be undone.',
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      centered: true,
      onOk() {
        props.onClose();
        dispatch(leaveRoom(currentRoom!.chatRoomId));
      },
      onCancel() {
        console.log('Cancel');
      }
    });
  };

  const handleOpenInviteModal = async () => {
    setMemberModalVisible(true);
    setInviteListLoading(true);

    try {
      const { data } = await request<RoomUserType[]>({
        method: 'GET',
        url: `/getListofUserToInvite/${currentRoom!.chatRoomId}`
      });
      setOtherUsers(data || []);
    } catch (error) {
      console.log((error as Error).message);
    } finally {
      setInviteListLoading(false);
    }
  };

  return (
    <Drawer
      className="room-drawer"
      placement="right"
      getContainer={false}
      visible={props.visible}
      onClose={props.onClose}
      closable={false}
      style={{ position: 'absolute' }}
    >
      <div style={{ position: 'relative' }}>
        {fetchUsersLoading && <Loading />}
        <section>
          <h3>Room Members</h3>
          {normalUsers.map((each) => (
            <div key={each.userId} className="user">
              <Avatar
                src={each.userAvatar}
                style={{ backgroundColor: randomColor(each.userId % COLOR_LISTS_LENGTH) }}
              >
                {each.userName[0]}
              </Avatar>
              <span className="name">{each.userName}</span>

              {each.userId === user!.id ? (
                <Tag color="blue" style={{ marginLeft: 'auto' }}>
                  You
                </Tag>
              ) : (
                <Dropdown overlay={renderMenu(false, each)} placement="bottomLeft">
                  <Button type="link" icon={<MoreOutlined />} style={{ marginLeft: 'auto' }} />
                </Dropdown>
              )}
            </div>
          ))}

          {isAdmin && currentRoom!.isPrivate && (
            <div style={{ textAlign: 'center' }}>
              <Button type="dashed" icon={<PlusOutlined />} onClick={() => handleOpenInviteModal()}>
                Invite Member
              </Button>
            </div>
          )}
        </section>

        <section style={{ marginTop: '2rem' }}>
          <h3>Blocked List</h3>
          {blockedUsers.map((each) => (
            <div key={each.userId} className="user">
              <Avatar
                src={each.userAvatar}
                style={{ backgroundColor: randomColor(each.userId % COLOR_LISTS_LENGTH) }}
              >
                {each.userName[0]}
              </Avatar>
              <span className="name">{each.userName}</span>

              {each.userId === user!.id ? (
                <Tag color="blue" style={{ marginLeft: 'auto' }}>
                  You
                </Tag>
              ) : (
                <Dropdown overlay={renderMenu(true, each)} placement="bottomLeft">
                  <Button type="link" icon={<MoreOutlined />} style={{ marginLeft: 'auto' }} />
                </Dropdown>
              )}
            </div>
          ))}
        </section>
      </div>

      <div style={{ marginTop: '2rem', textAlign: 'center' }}>
        <Button type="primary" danger onClick={showDeleteConfirm}>
          Leave Room
        </Button>
      </div>

      <Modal
        visible={memberModalVisible}
        onCancel={() => setMemberModalVisible(false)}
        closable
        title="Invite Members"
        centered
        footer={null}
        width={300}
      >
        <div className="member-modal">
          {inviteListLoading && <Loading />}
          {otherUsers.map((each) => (
            <div key={each.userId} className="user">
              <Avatar
                src={each.userAvatar}
                style={{ backgroundColor: randomColor(each.userId % COLOR_LISTS_LENGTH) }}
              >
                {each.userName[0]}
              </Avatar>
              <span className="name">{each.userName}</span>
              <Popconfirm
                title="Confirm to send invitation"
                onConfirm={async () => {
                  try {
                    await request<string, { user: number; chatRoomId: number }>({
                      method: 'POST',
                      url: '/sendInvite',
                      body: {
                        user: each.userId,
                        chatRoomId: currentRoom!.chatRoomId
                      }
                    });
                    message.success('Invitation Sent');
                  } catch (error) {
                    console.log((error as Error).message);
                  }
                }}
                okText="Invite"
                cancelText="Cancel"
              >
                <Button
                  type="primary"
                  size="small"
                  style={{ marginLeft: 'auto' }}
                  icon={<PlusOutlined />}
                >
                  Invite
                </Button>
              </Popconfirm>
            </div>
          ))}
        </div>
      </Modal>
    </Drawer>
  );
}
