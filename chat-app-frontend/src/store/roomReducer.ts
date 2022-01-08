import { RoomActionType } from '../@types/enums';

const initialState: RoomState = {
  currentRoom: null,
  messages: [],
  messageLoading: false,
  normalUsers: [],
  blockedUsers: [],
  fetchUsersLoading: false,
  isAdmin: false,
  isBlocked: false,
  isPrivate: false,
  isPrivateMessage: false,
  privateMessageTo: null
};

export default (state: RoomState = initialState, { type, payload }: RoomAction): RoomState => {
  switch (type) {
    case RoomActionType.ACTIVE_ROOM:
      return {
        ...state,
        currentRoom: payload
      };
    case RoomActionType.SET_PRIVATE_MESSAGE:
      return {
        ...state,
        isPrivateMessage: true,
        privateMessageTo: payload
      };
    case RoomActionType.UNSET_PRIVATE_MESSAGE:
      return {
        ...state,
        isPrivateMessage: false,
        privateMessageTo: null
      };
    case RoomActionType.FETCH_ROOM:
      return {
        ...state,
        messages: payload.messages,
        isAdmin: payload.isAdmin,
        isBlocked: payload.isBlocked,
        isPrivate: payload.isPrivate,
        messageLoading: false
      };
    case RoomActionType.FETCH_ROOM_LOADING:
      return {
        ...state,
        messageLoading: true
      };
    case RoomActionType.CANCEL_LOADING:
      return {
        ...state,
        messageLoading: false
      };
    case RoomActionType.FETCH_NORMAL_USERS:
      return {
        ...state,
        normalUsers: payload
      };
    case RoomActionType.FETCH_BLOCKED_USERS:
      return {
        ...state,
        blockedUsers: payload
      };
    case RoomActionType.FETCH_USERS_LOADING:
      return {
        ...state,
        fetchUsersLoading: true
      };
    case RoomActionType.FETCH_USERS_END_LOADING:
      return {
        ...state,
        fetchUsersLoading: false
      };
    case RoomActionType.NEW_MESSAGE:
      return {
        ...state,
        messages: [...state.messages, payload]
      };
    case RoomActionType.EDIT_MESSAGE:
      const messagesCopy1: MessageType[] = [...state.messages];
      messagesCopy1.find((each) => each.messageId === payload.messageId)!.content = payload.content;
      return {
        ...state,
        messages: messagesCopy1
      };
    case RoomActionType.REMOVE_MESSAGE:
      const messagesCopy2: MessageType[] = [...state.messages];
      messagesCopy2.splice(
        state.messages.findIndex((each) => each.messageId === payload.messageId),
        1
      );
      return {
        ...state,
        messages: messagesCopy2
      };
    case RoomActionType.LEAVE_ROOM:
      return {
        ...state,
        currentRoom: null,
        messages: [],
        messageLoading: false,
        normalUsers: [],
        blockedUsers: [],
        isAdmin: false,
        isPrivateMessage: false,
        isBlocked: false,
        isPrivate: false,
        privateMessageTo: null
      };
    default:
      return state;
  }
};
