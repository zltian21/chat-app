import React, { useEffect, useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { RouteChildrenProps } from 'react-router';
import { FaComments, FaHashtag, FaBell, FaSignOutAlt } from 'react-icons/fa';
import { Avatar, Badge, Modal, Popover, Tooltip } from 'antd';
import { ExclamationCircleOutlined, StopOutlined } from '@ant-design/icons';

import NotificationList from './NotificationList';
import { RootState } from '../../store';
import {
  fetchNotifications,
  fetchRoomList,
  fetchUserInfo,
  userLogout
} from '../../store/actions/user';
import { COLOR_LISTS_LENGTH, randomColor } from '../../utils/random';
import { NotificationStatusEnum, UserActionType } from '../../@types/enums';

import './styles.scss';

export default function Sidebar(props: RouteChildrenProps): JSX.Element {
  const dispatch = useDispatch();
  const { user, sidebarTab, notifications, closedWarningPanel } = useSelector(
    (state: RootState) => state.user
  );

  const [showNotifications, setShowNotifications] = useState<boolean>(false);

  useEffect(() => {
    let timer: NodeJS.Timeout;

    if (user) {
      const intervalBody = () => {
        dispatch(fetchUserInfo());
        dispatch(fetchNotifications());
      };
      intervalBody();
      timer = setInterval(intervalBody, 3000);
      dispatch(fetchRoomList('my'));
    }

    return () => clearInterval(timer);
  }, []);

  useEffect(() => {
    if (user) {
      if (user.hateSpeechCount >= 5 && user.hateSpeechCount < 10 && !closedWarningPanel) {
        Modal.confirm({
          title: 'Warning',
          icon: <ExclamationCircleOutlined />,
          content: `You have been detected sending hate speech ${user.hateSpeechCount} times. Over 10 times and you will be banned from this app.`,
          centered: true
        });
        dispatch({ type: UserActionType.CLOSE_WARNING_PANEL });
      } else if (user.hateSpeechCount >= 10 && !closedWarningPanel) {
        Modal.confirm({
          title: 'Banned',
          icon: <StopOutlined />,
          content:
            'You have exceeded the maximum of speech count. You will be forced to logout and banned',
          centered: true,
          closable: false,
          cancelText: '',
          onOk: () => {
            dispatch({ type: UserActionType.USER_LOGOUT });
          },
          onCancel: () => {
            dispatch({ type: UserActionType.USER_LOGOUT });
          }
        });
        dispatch({ type: UserActionType.CLOSE_WARNING_PANEL });
      }
    }
  }, [user]);

  const handleSwitchTab = (tab: 'my' | 'public') => {
    dispatch(fetchRoomList(tab));
  };

  return (
    <div className="sidebar">
      <Popover
        trigger="hover"
        placement="rightTop"
        arrowContent
        content={
          <div className="user-info">
            <div>
              <span>Username:</span> {user?.username}
            </div>
            <div>
              <span>Age:</span> {user?.age}
            </div>
            <div>
              <span>School:</span> {user?.schoolName}
            </div>
            <div>
              <span>Interests:</span> {user?.interests}
            </div>
            <hr />
            <div>
              <span>HSC:</span> {user?.hateSpeechCount}
            </div>
          </div>
        }
      >
        <Avatar
          size={64}
          src={user?.imageUrl}
          style={{ backgroundColor: randomColor(user?.id || 0 % COLOR_LISTS_LENGTH) }}
        />
      </Popover>
      <ul>
        <li className={`${sidebarTab === 'my' ? 'active' : ''}`}>
          <Tooltip placement="left" title="Chatrooms I Joined">
            <Badge>
              <FaComments size={30} onClick={() => handleSwitchTab('my')} />
            </Badge>
          </Tooltip>
        </li>
        <li className={`${sidebarTab === 'public' ? 'active' : ''}`}>
          <Tooltip placement="left" title="Other Public Chatrooms">
            <Badge>
              <FaHashtag size={30} onClick={() => handleSwitchTab('public')} />
            </Badge>
          </Tooltip>
        </li>
        <li className={showNotifications ? 'active' : ''}>
          <Popover
            trigger="click"
            placement="right"
            title="Notifications"
            arrowContent
            content={<NotificationList />}
            onVisibleChange={setShowNotifications}
          >
            <Badge
              count={
                notifications.filter(
                  (each) =>
                    each.type === 'invite' && each.status === NotificationStatusEnum.NO_STATUS
                ).length
              }
            >
              <FaBell size={30} />
            </Badge>
          </Popover>
        </li>
        <li>
          <Tooltip placement="left" title="Log out">
            <FaSignOutAlt
              style={{ transform: 'rotate(180deg)', marginTop: 'auto' }}
              size={30}
              onClick={() => {
                props.history.push('/login');
                dispatch(userLogout());
              }}
            />
          </Tooltip>
        </li>
      </ul>
    </div>
  );
}
