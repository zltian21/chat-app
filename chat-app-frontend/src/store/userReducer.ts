import { UserActionType } from '../@types/enums';
import { generateWebsocket } from '../utils/websocket';

const initialState: UserState = {
  token: localStorage.getItem('token'),
  isAuthenticated: !!localStorage.getItem('token'),
  user: null,
  notifications: [],
  roomList: [],
  roomListLoading: false,
  sidebarTab: 'my',
  isLoading: false,
  ws: null,
  closedWarningPanel: false
};

export default (state = initialState, { type, payload }: UserAction): UserState => {
  switch (type) {
    case UserActionType.USER_LOGIN:
    case UserActionType.USER_REGISTER:
      localStorage.setItem('token', `${payload.id}`);
      state.ws?.close();
      return {
        ...state,
        token: `${payload!.id}`,
        isAuthenticated: true,
        user: payload,
        sidebarTab: 'my',
        isLoading: false,
        ws: generateWebsocket(payload.username)
      };
    case UserActionType.USER_LOADING:
      return {
        ...state,
        isLoading: true
      };
    case UserActionType.USER_LOGOUT:
      localStorage.removeItem('token');
      return {
        ...state,
        user: null,
        token: null,
        isAuthenticated: false,
        notifications: [],
        roomList: [],
        ws: null
      };
    case UserActionType.USER_UPDATE:
      return {
        ...state,
        user: payload,
        ws: state.ws ? state.ws : generateWebsocket(payload.username)
      };
    case UserActionType.USER_ERROR:
      return {
        ...state,
        isLoading: false
      };
    case UserActionType.FETCH_NOTIFICATION:
      return {
        ...state,
        notifications: payload
      };
    case UserActionType.UPDATE_NOTIFICATION:
      const index = state.notifications.findIndex((each) => each.id === payload.id);
      const notifications = [...state.notifications];
      notifications[index].status = payload.status;
      return {
        ...state,
        notifications
      };
    case UserActionType.FETCH_CHATROOM_LIST:
      return {
        ...state,
        roomList: payload.map((each: RoomListItemType) => ({ ...each, hasNewMessage: false })),
        roomListLoading: false
      };
    case UserActionType.LOADING_CHATROOM:
      return {
        ...state,
        roomListLoading: true
      };
    case UserActionType.SET_SIDEBAR_TAB:
      return {
        ...state,
        sidebarTab: payload
      };
    case UserActionType.LEAVE_ALL_ROOMS:
      return {
        ...state,
        roomList: []
      };
    case UserActionType.CLOSE_WARNING_PANEL:
      return {
        ...state,
        closedWarningPanel: true
      };
    case UserActionType.OPEN_WARNING_PANEL:
      return {
        ...state,
        closedWarningPanel: false
      };
    case UserActionType.HAVE_NEW_MESSAGE:
      const roomListCopy1 = [...state.roomList];
      roomListCopy1.find((each) => each.chatRoomId === payload.chatRoomId)!.hasNewMessage = true;
      return {
        ...state,
        roomList: roomListCopy1
      };
    case UserActionType.CLEAR_NEW_MESSAGE:
      const roomListCopy2 = [...state.roomList];
      roomListCopy2.find((each) => each.chatRoomId === payload)!.hasNewMessage = false;
      return {
        ...state,
        roomList: roomListCopy2
      };
    case UserActionType.RECONNECT_WEBSOCKET:
      return {
        ...state,
        ws: state.user ? generateWebsocket(state.user.username) : null
      };
    default: {
      return state;
    }
  }
};
