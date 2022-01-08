import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Avatar, Button, Tag, Modal, Form, Input, InputNumber, Switch, Badge } from 'antd';
import { useForm } from 'antd/lib/form/Form';
import { ExclamationCircleOutlined, PlusOutlined } from '@ant-design/icons';

import { Loading } from '../../Components/Loading';

import { randomColor } from '../../utils/random';
import request from '../../utils/request';
import { RootState } from '../../store';
import { fetchRoomList } from '../../store/actions/user';
import { RoomActionType, UserActionType } from '../../@types/enums';

export function RoomsList(): JSX.Element {
  const dispatch = useDispatch();
  const data = useSelector((state: RootState) => state.user.roomList);
  const roomListLoading = useSelector((state: RootState) => state.user.roomListLoading);
  const sidebarTab = useSelector((state: RootState) => state.user.sidebarTab);
  const currentRoom = useSelector((state: RootState) => state.room.currentRoom);

  const [form] = useForm();
  const [createModalVisible, setCreateModalVisible] = useState<boolean>(false);
  const [createLoading, setCreateLoading] = useState<boolean>(false);

  const showDeleteConfirm = () => {
    Modal.confirm({
      title: 'Are you sure to leave all rooms?',
      icon: <ExclamationCircleOutlined />,
      content: 'This operation cannot be undone.',
      okText: 'Yes',
      okType: 'danger',
      cancelText: 'No',
      centered: true,
      async onOk() {
        try {
          await request<string>({
            method: 'POST',
            url: '/leaveAllChatroom'
          });
          dispatch({ type: UserActionType.LEAVE_ALL_ROOMS });
          dispatch({ type: RoomActionType.LEAVE_ROOM });
        } catch (error) {
          console.log((error as Error).message);
        }
      },
      onCancel() {
        console.log('Cancel');
      }
    });
  };

  const handleSubmitForm = async () => {
    try {
      const valid = await form.validateFields();

      if (valid) {
        setCreateLoading(true);

        await request<string, CreatRoomRequest>({
          method: 'POST',
          url: '/createRoom',
          body: form.getFieldsValue()
        });

        if (sidebarTab === 'my') {
          dispatch(fetchRoomList('my'));
        }

        setCreateModalVisible(false);
      }
    } catch (error) {
      console.log((error as Error).message);
    } finally {
      setCreateLoading(false);
    }
  };

  const handleClickRoom = async (id: number) => {
    try {
      if (sidebarTab === 'public') {
        // Join room
        await request<string, { chatRoomId: number }>({
          method: 'POST',
          url: '/joinRoom',
          body: {
            chatRoomId: id
          }
        });
      }

      dispatch({
        type: RoomActionType.ACTIVE_ROOM,
        payload: data.find((each) => each.chatRoomId === id)
      });
      dispatch({ type: UserActionType.CLEAR_NEW_MESSAGE, payload: id });
    } catch (error) {
      console.log((error as Error).message);
    }
  };

  return (
    <div className="rooms-list">
      <Button
        type="dashed"
        size="large"
        icon={<PlusOutlined />}
        style={{ margin: '0 1rem' }}
        onClick={() => setCreateModalVisible(true)}
      >
        Create a Room
      </Button>
      <div className="list">
        {roomListLoading && <Loading />}
        {data.map((each) => (
          <Room
            key={each.chatRoomId}
            name={each.chatRoomName}
            id={each.chatRoomId}
            hasNewMessage={each.hasNewMessage}
            isActive={currentRoom?.chatRoomId === each.chatRoomId}
            isPrivate={each.isPrivate}
            onClick={handleClickRoom}
          />
        ))}
      </div>
      <Button type="primary" color="danger" danger onClick={showDeleteConfirm}>
        Leave All Rooms
      </Button>

      <Modal
        title="Create a Room"
        visible={createModalVisible}
        centered
        okText="Create"
        onCancel={() => setCreateModalVisible(false)}
        destroyOnClose
        footer={
          <>
            <Button onClick={() => setCreateModalVisible(false)}>Cancel</Button>
            <Button type="primary" onClick={() => handleSubmitForm()} loading={createLoading}>
              Create
            </Button>
          </>
        }
      >
        <Form
          form={form}
          requiredMark={false}
          className="form"
          labelAlign="right"
          labelCol={{ span: 6 }}
          wrapperCol={{ span: 12 }}
        >
          <Form.Item
            name="roomName"
            label="Name"
            rules={[{ required: true, message: 'This field cannot be empty' }]}
          >
            <Input placeholder="Room Name" />
          </Form.Item>

          <Form.Item
            name="roomSize"
            label="Size"
            initialValue={10}
            rules={[
              { required: true, message: 'This field cannot be empty' },
              {
                validator(_, value) {
                  if (value !== null && (+value < 2 || +value > 70)) {
                    return Promise.reject(new Error('Invalid size'));
                  }
                  return Promise.resolve();
                }
              }
            ]}
          >
            <InputNumber min={2} max={70} />
          </Form.Item>

          <Form.Item
            name="isPrivate"
            label="Is Private"
            valuePropName="checked"
            initialValue={true}
          >
            <Switch />
          </Form.Item>
        </Form>
      </Modal>
    </div>
  );
}

interface RoomProps {
  id: number;
  name: string;
  hasNewMessage: boolean;
  isActive: boolean;
  isPrivate: boolean;
  onClick: (id: number) => void;
}

export function Room(props: RoomProps): JSX.Element {
  return (
    <div
      className={`room ${props.isActive && 'active'}`}
      onClick={() => !props.isActive && props.onClick(props.id)}
    >
      <Badge dot={props.hasNewMessage}>
        <Avatar size={48} shape="square" style={{ backgroundColor: randomColor(props.id % 8) }}>
          {props.name
            .split(' ')
            .map((each) => each[0])
            .join('')
            .slice(0, 2)}
        </Avatar>
      </Badge>
      <div className="info">
        <span className="name">{props.name}</span>
        <br />
        {props.isPrivate ? <Tag color="blue">Private</Tag> : <Tag color="green">Public</Tag>}
      </div>
    </div>
  );
}
