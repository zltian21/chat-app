import React, { useState, useRef, useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { CloseCircleFilled, EllipsisOutlined } from '@ant-design/icons';
import { Button, Input, message, Spin, Tag } from 'antd';

import Message from '../../Components/Message';
import { RootState } from '../../store';
import { editMessage, fetchRoomInfo, newMessage, removeMessage } from '../../store/actions/room';
import { RoomActionType } from '../../@types/enums';

import './styles.scss';

interface ChatPanelProps {
  openDrawer: () => void;
}

export default function ChatPanel(props: ChatPanelProps): JSX.Element {
  const dispatch = useDispatch();
  const { user } = useSelector((state: RootState) => state.user);
  const {
    currentRoom,
    messages,
    messageLoading,
    isPrivateMessage,
    privateMessageTo,
    isBlocked,
    isAdmin
  } = useSelector((state: RootState) => state.room);

  const messagePanelRef = useRef<HTMLDivElement>(null);

  const [inputMessage, setInputMessage] = useState<string>('');
  const [scroll, setScroll] = useState<boolean>(false);

  useEffect(() => {
    if (currentRoom) {
      dispatch(fetchRoomInfo(currentRoom.chatRoomId));
    }
  }, [currentRoom]);

  useEffect(() => {
    if (messagePanelRef?.current && !scroll) {
      const { current } = messagePanelRef;
      current.scrollIntoView({ behavior: 'smooth' });
    }
  }, [messages]);

  useEffect(() => {
    if (scroll) {
      setTimeout(() => setScroll(false), 2000);
    }
  }, [scroll]);

  const handleKeyPress = (e: any) => {
    if (isBlocked) {
      message.error('You are blocked');
      return;
    }

    if (e.which === 13 && !e.shiftKey && inputMessage) {
      e.preventDefault();
      dispatch(
        newMessage({
          messageId: Math.random(),
          content: inputMessage,
          isPrivate: isPrivateMessage,
          sender: {
            userId: user!.id,
            userName: user!.username,
            userAvatar: user!.imageUrl
          },
          type: 'new',
          receiver: privateMessageTo
        })
      );
      setInputMessage('');
    }
  };

  const handleMessageChange = (id: number, text: string) => {
    dispatch(editMessage(id, text, currentRoom!.chatRoomId));
  };

  const handleMessageDelete = (id: number) => {
    dispatch(removeMessage(id, currentRoom!.chatRoomId));
  };

  const turnOffPrivateMessage = () => {
    dispatch({ type: RoomActionType.UNSET_PRIVATE_MESSAGE });
  };

  if (!currentRoom) {
    return (
      <div className="placeholder-panel">
        <h1>Select a room to start chatting!</h1>
        <img
          src="https://res.cloudinary.com/rylanzhou/image/upload/v1635557590/COMP%20504/undraw_Anonymous_feedback_re_rc5v_zn7vcm.png"
          alt="Placeholder"
        />
      </div>
    );
  }

  return (
    <div className="chat-panel">
      <div className="header">
        <h3>{currentRoom!.chatRoomName}</h3>
        {currentRoom!.isPrivate ? <Tag color="blue">Private</Tag> : <Tag color="green">Public</Tag>}
        <Button
          type="link"
          icon={<EllipsisOutlined style={{ fontSize: 32 }} />}
          style={{ marginLeft: 'auto' }}
          onClick={() => props.openDrawer()}
        />
      </div>

      <div className="message-panel" onScroll={() => setScroll(true)}>
        {messageLoading ? (
          <Spin
            style={{
              position: 'absolute',
              left: '50%',
              top: '2rem',
              transform: 'translateX(-50%)'
            }}
          />
        ) : (
          <>
            {messages.map((each) => (
              <Message
                key={each.messageId}
                message={each}
                isAdmin={isAdmin}
                own={each.sender?.userId === user?.id}
                onMessageChange={handleMessageChange}
                onMessageDelete={handleMessageDelete}
              />
            ))}
          </>
        )}
        <div style={{ float: 'left', clear: 'both' }} ref={messagePanelRef}></div>
      </div>

      <div className="operation-panel">
        {isPrivateMessage && (
          <div className="private-banner">
            <CloseCircleFilled onClick={turnOffPrivateMessage} />
            Private Message to {privateMessageTo!.userName}
          </div>
        )}
        <Input.TextArea
          disabled={isBlocked}
          placeholder={isBlocked ? 'You are blocked' : ''}
          onKeyPress={handleKeyPress}
          value={inputMessage}
          onChange={(e) => setInputMessage(e.target.value)}
          showCount
          maxLength={100}
          rows={isPrivateMessage ? 6 : 7}
        />
      </div>
    </div>
  );
}
