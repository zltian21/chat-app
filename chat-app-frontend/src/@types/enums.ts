export enum NotificationStatusEnum {
  ACCEPTED,
  DECLINED,
  NO_STATUS
}

export enum UserActionType {
  USER_LOGIN = 'USER_LOGIN',
  USER_REGISTER = 'USER_REGISTER',
  USER_LOGOUT = 'USER_LOGOUT',
  USER_UPDATE = 'USER_UPDATE',
  USER_ERROR = 'USER_ERROR',
  USER_LOADING = 'USER_LOADING',
  FETCH_NOTIFICATION = 'FETCH_NOTIFICATION',
  UPDATE_NOTIFICATION = 'UPDATE_NOTIFICATION',
  FETCH_CHATROOM_LIST = 'FETCH_CHATROOM_LIST',
  LOADING_CHATROOM = 'LOADING_CHATROOM',
  SET_SIDEBAR_TAB = 'SET_SIDEBAR_TAB',
  LEAVE_ALL_ROOMS = 'LEAVE_ALL_ROOMS',
  HAVE_NEW_MESSAGE = 'HAVE_NEW_MESSAGE',
  CLEAR_NEW_MESSAGE = 'CLEAR_NEW_MESSAGE',
  OPEN_WARNING_PANEL = 'OPEN_WARNING_PANEL',
  CLOSE_WARNING_PANEL = 'CLOSE_WARNING_PANEL',
  RECONNECT_WEBSOCKET = 'RECONNECT_WEBSOCKET'
}

export enum RoomActionType {
  ACTIVE_ROOM = 'ACTIVE_ROOM',
  FETCH_ROOM = 'FETCH_ROOM',
  FETCH_ROOM_LOADING = 'FETCH_ROOM_LOADING',
  CANCEL_LOADING = 'CANCEL_LOADING',
  FETCH_NORMAL_USERS = 'FETCH_NORMAL_USERS',
  FETCH_BLOCKED_USERS = 'FETCH_BLOCKED_USERS',
  FETCH_USERS_LOADING = 'FETCH_USERS_LOADING',
  FETCH_USERS_END_LOADING = 'FETCH_USERS_END_LOADING',
  SET_PRIVATE_MESSAGE = 'SET_PRIVATE_MESSAGE',
  UNSET_PRIVATE_MESSAGE = 'UNSET_PRIVATE_MESSAGE',
  NEW_MESSAGE = 'NEW_MESSAGE',
  EDIT_MESSAGE = 'EDIT_MESSAGE',
  REMOVE_MESSAGE = 'REMOVE_MESSAGE',
  LEAVE_ROOM = 'LEAVE_ROOM'
}
