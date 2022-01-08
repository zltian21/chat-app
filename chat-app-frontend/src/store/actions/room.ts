import { RootState } from '..';
import { RoomActionType } from '../../@types/enums';
import request from '../../utils/request';
import { fetchRoomList } from './user';

export const fetchRoomInfo = (id: number) => async (dispatch: DispatchType<RoomAction>) => {
  try {
    dispatch({ type: RoomActionType.FETCH_ROOM_LOADING });
    const { data } = await request<GetRoomResponse[]>({
      method: 'GET',
      url: `/getChatRoom/${id}`
    });

    dispatch({ type: RoomActionType.FETCH_ROOM, payload: data![0] });
  } catch (error) {
    console.log((error as Error).message);
  } finally {
    dispatch({ type: RoomActionType.CANCEL_LOADING });
  }
};

export const fetchRoomUsers = (id: number) => async (dispatch: DispatchType<RoomAction>) => {
  try {
    const data1 = await request<RoomUserType[]>({
      method: 'GET',
      url: `/getListofUser/${id}`
    });

    dispatch({ type: RoomActionType.FETCH_NORMAL_USERS, payload: data1.data });

    const data2 = await request<RoomUserType[]>({
      method: 'GET',
      url: `/getListofBlockedUser/${id}`
    });

    dispatch({ type: RoomActionType.FETCH_BLOCKED_USERS, payload: data2.data });
  } catch (error) {
    console.log((error as Error).message);
  } finally {
    dispatch({ type: RoomActionType.FETCH_USERS_END_LOADING });
  }
};

export const leaveRoom = (id: number) => async (dispatch: DispatchType<RoomAction>) => {
  try {
    await request<string, { chatRoomId: number }>({
      method: 'POST',
      url: '/leaveChatRoom',
      body: { chatRoomId: id }
    });
    dispatch({ type: RoomActionType.LEAVE_ROOM });
    await fetchRoomList('my')(dispatch);
  } catch (error) {
    console.log((error as Error).message);
  }
};

export const newMessage =
  (message: MessageType) => (_: DispatchType<RoomAction>, getState: () => RootState) => {
    const { ws } = getState().user;
    const { currentRoom } = getState().room;

    const processed: any = {
      ...message,
      senderId: message.sender?.userId,
      receiverId: message.receiver?.userId || -1,
      roomId: currentRoom?.chatRoomId
    };
    ws?.send(JSON.stringify(processed));

    // dispatch({ type: RoomActionType.NEW_MESSAGE, payload: message });
  };

export const editMessage = (messageId: number, content: string, chatRoomId: number) => async () => {
  try {
    await request<string, { messageId: number; content: string; chatRoomId: number }>({
      method: 'POST',
      url: '/editMessage',
      body: { messageId, content, chatRoomId }
    });
  } catch (error) {
    console.log((error as Error).message);
  }
};

export const removeMessage = (messageId: number, chatRoomId: number) => async () => {
  try {
    await request<string, { messageId: number; chatRoomId: number }>({
      method: 'POST',
      url: '/removeMessage',
      body: { messageId, chatRoomId }
    });
  } catch (error) {
    console.log((error as Error).message);
  }
};
