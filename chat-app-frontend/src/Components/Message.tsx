import React, { useState } from 'react';
import { Alert, Avatar, Divider, Input, Popover } from 'antd';
import { COLOR_LISTS_LENGTH, randomColor } from '../utils/random';
import { EyeInvisibleOutlined } from '@ant-design/icons';

interface MessageProps {
  own: boolean;
  isAdmin: boolean;
  message: MessageType;
  onMessageChange: (id: number, text: string) => void;
  onMessageDelete: (id: number) => void;
}

export default function Message({
  own,
  isAdmin,
  message,
  onMessageChange,
  onMessageDelete
}: MessageProps): JSX.Element {
  const [isEditing, setIsEditing] = useState<boolean>(false);
  const [editText, setEditText] = useState<string>('');

  const handleEditOnKeyPress = (e: any) => {
    if (e.which === 13 && !e.shiftKey && editText) {
      e.preventDefault();
      onMessageChange(message.messageId, editText);
    }
  };

  const renderPopoverContent = () => {
    return (
      <div className="message-operation">
        <span
          onClick={() => {
            setIsEditing(true);
            setEditText(message.content);
          }}
        >
          Edit
        </span>
        <Divider type="vertical" />
        <span onClick={() => onMessageDelete(message.messageId)}>Remove</span>
        {isEditing && (
          <Input
            value={editText}
            onChange={(e) => setEditText(e.target.value)}
            onKeyPress={handleEditOnKeyPress}
          />
        )}
      </div>
    );
  };

  if (message.type === 'system') {
    return (
      <Alert message={message.content} type="warning" showIcon style={{ marginBottom: '1rem' }} />
    );
  }

  if (own) {
    return (
      <div className="message-item me" key={message.messageId}>
        <div className="info">
          <div className="name">
            {message.isPrivate && (
              <span style={{ color: 'rgb(93, 52, 139)' }}>
                Private to {message.receiver!.userName}
                <EyeInvisibleOutlined style={{ margin: '0 1rem' }} />
              </span>
            )}
            {message.sender!.userName}
          </div>

          <Popover
            content={renderPopoverContent()}
            trigger="click"
            onVisibleChange={(visible) => !visible && setIsEditing(false)}
          >
            <div className="text">{message.content}</div>
          </Popover>
        </div>
        <Avatar
          size="large"
          src={message.sender!.userAvatar}
          style={{
            marginTop: '.3rem',
            backgroundColor: randomColor(message.sender!.userId % COLOR_LISTS_LENGTH)
          }}
        >
          {message.sender!.userName[0]}
        </Avatar>
      </div>
    );
  }
  return (
    <div className="message-item" key={message.messageId}>
      <Avatar
        size="large"
        src={message.sender?.userAvatar}
        style={{
          marginTop: '.3rem',
          backgroundColor: randomColor(message.sender!.userId % COLOR_LISTS_LENGTH)
        }}
      >
        {message.sender?.userName[0]}
      </Avatar>
      <div className="info">
        <div className="name">
          {message.sender?.userName}
          {message.isPrivate && (
            <span style={{ color: 'rgb(93, 52, 139)' }}>
              <EyeInvisibleOutlined style={{ margin: '0 1rem' }} />
              Private to ME
            </span>
          )}{' '}
        </div>

        {isAdmin ? (
          <Popover content={renderPopoverContent()} trigger="click">
            <div className="text"> {message.content}</div>
          </Popover>
        ) : (
          <div className="text"> {message.content}</div>
        )}
      </div>
    </div>
  );
}
