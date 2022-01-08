type DispatchType<T> = import('redux-thunk').ThunkDispatch<any, any, T>;

interface UserState {
  token: string | null;
  isAuthenticated: boolean;
  user: UserType | null;
  notifications: NotificationType[];
  roomList: RoomListItemType[];
  roomListLoading: boolean;
  sidebarTab: 'my' | 'public';
  isLoading: boolean;
  ws: WebSocket | null;
  closedWarningPanel: boolean;
}

interface UserAction {
  type: import('./enums').UserActionType;
  payload?: any;
}

interface RoomState {
  currentRoom: RoomListItemType | null;
  messages: MessageType[];
  messageLoading: boolean;
  fetchUsersLoading: boolean;
  blockedUsers: RoomUserType[];
  normalUsers: RoomUserType[];
  isAdmin: boolean;
  isBlocked: boolean;
  isPrivate: boolean;
  isPrivateMessage: boolean;
  privateMessageTo: RoomUserType | null;
}

interface RoomAction {
  type: import('./enums').RoomActionType;
  payload?: any;
}
