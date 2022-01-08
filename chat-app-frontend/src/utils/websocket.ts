import { notification } from 'antd';
import { RoomActionType, UserActionType } from '../@types/enums';
import store from '../store';

export const generateWebsocket = (username: string) => {
  const prefix = location.protocol.includes('https') ? 'wss' : 'ws';

  const ws = new WebSocket(
    `${prefix}://${location.hostname}:${location.port}/chatapp?username=${username}`
  );

  ws.onopen = () => {
    console.log('Websocket Established');
  };

  ws.onmessage = ({ data }: MessageEvent<string>) => {
    const obj: MessageType = JSON.parse(data);

    if (store.getState().room.currentRoom?.chatRoomId !== obj.chatRoomId) {
      store.dispatch({ type: UserActionType.HAVE_NEW_MESSAGE, payload: obj.chatRoomId });
      return;
    }
    if (obj.type === 'edit') {
      store.dispatch({ type: RoomActionType.EDIT_MESSAGE, payload: obj });
    } else if (obj.type === 'remove') {
      store.dispatch({ type: RoomActionType.REMOVE_MESSAGE, payload: obj });
    } else {
      store.dispatch({ type: RoomActionType.NEW_MESSAGE, payload: obj });
    }
  };

  ws.onclose = () => {
    console.log('Websocket Closed');
    store.dispatch({ type: UserActionType.RECONNECT_WEBSOCKET });
  };

  ws.onerror = (err) => {
    notification.error({
      message: 'Websocket Error'
    });
    console.log(err);
  };

  return ws;
};
