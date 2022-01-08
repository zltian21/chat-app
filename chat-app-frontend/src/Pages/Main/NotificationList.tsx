import React from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Button, List } from 'antd';
import { BellTwoTone, CheckOutlined, CloseOutlined, MailTwoTone } from '@ant-design/icons';

import { RootState } from '../../store';
import { NotificationStatusEnum } from '../../@types/enums';

import './styles.scss';
import request from '../../utils/request';
import { fetchRoomList, updateInvitation } from '../../store/actions/user';

export default function NotificationList(): JSX.Element {
  const dispatch = useDispatch();
  const data = useSelector((state: RootState) => state.user.notifications);
  const tab = useSelector((state: RootState) => state.user.sidebarTab);

  const handleInvitationOp = async (id: number, accepted: boolean) => {
    try {
      await request<string, { accept: boolean }>({
        method: 'POST',
        url: `/opInvitation/${id}`,
        body: { accept: accepted }
      });
      dispatch(updateInvitation(id, accepted));
      if (accepted && tab === 'my') {
        dispatch(fetchRoomList('my'));
      }
    } catch (error) {
      console.log((error as Error).message);
    }
  };

  const renderOperations = ({ id, type, status }: NotificationType) => {
    if (type === 'invite')
      return (
        <div className="operation">
          {status === NotificationStatusEnum.NO_STATUS ? (
            <>
              <Button
                danger
                shape="circle"
                size="small"
                icon={<CloseOutlined />}
                onClick={() => handleInvitationOp(id, false)}
              />{' '}
              <Button
                type="primary"
                shape="circle"
                size="small"
                icon={<CheckOutlined />}
                onClick={() => handleInvitationOp(id, true)}
              />
            </>
          ) : status === NotificationStatusEnum.ACCEPTED ? (
            <span>Accepted</span>
          ) : (
            <span>Declined</span>
          )}
        </div>
      );
    return null;
  };

  const processContent = (content: string) => {
    const index = content.indexOf('[');
    const match = content.match(/\[(.*)\]/);

    return (
      <>
        {content.substring(0, index)} <strong>{match![1]}</strong>
      </>
    );
  };

  return (
    <div className="notifications-list">
      <List
        dataSource={data}
        renderItem={(each) => (
          <List.Item className="notification-item">
            <div className="info">
              {each.type === 'invite' ? (
                <MailTwoTone className="bell" color="#ccc" />
              ) : (
                <BellTwoTone className="bell" color="#ccc" />
              )}
              {processContent(each.content)}
            </div>
            {renderOperations(each)}
          </List.Item>
        )}
      />
    </div>
  );
}
