import { RootState } from '..';
import { NotificationStatusEnum, UserActionType } from '../../@types/enums';
import request from '../../utils/request';

export const userLogin =
  (username: string, password: string) => async (dispatch: DispatchType<UserAction>) => {
    try {
      dispatch({ type: UserActionType.USER_LOADING });
      const { data } = await request<UserType[], UserLoginRequest>({
        method: 'POST',
        url: '/login',
        body: { username, password }
      });
      dispatch({ type: UserActionType.USER_LOGIN, payload: data![0] });
    } catch (error) {
      console.log((error as Error).message);
      dispatch({ type: UserActionType.USER_ERROR });
    }
  };

export const userRegister =
  (userInfo: UserRegisterRequest) => async (dispatch: DispatchType<UserAction>) => {
    try {
      dispatch({ type: UserActionType.USER_LOADING });
      const { data } = await request<UserType[], UserRegisterRequest>({
        method: 'POST',
        url: '/register',
        body: { ...userInfo }
      });
      dispatch({ type: UserActionType.USER_REGISTER, payload: data![0] });
    } catch (error) {
      console.log((error as Error).message);
      dispatch({ type: UserActionType.USER_ERROR, payload: (error as Error).message });
    }
  };

export const userLogout = () => async (dispatch: DispatchType<UserAction>) => {
  try {
    await request<string>({
      method: 'GET',
      url: '/logout'
    });
    dispatch({ type: UserActionType.USER_LOGOUT });
  } catch (error) {
    console.log((error as Error).message);
  }
};

export const fetchUserInfo =
  (id?: string) => async (dispatch: DispatchType<UserAction>, getState: () => RootState) => {
    try {
      const { data } = await request<UserType[]>({
        method: 'GET',
        url: `/getUserInfo/${id || getState().user.token}`
      });
      dispatch({ type: UserActionType.USER_UPDATE, payload: data![0] });
      if (
        getState().user.user &&
        data![0].hateSpeechCount > getState().user.user!.hateSpeechCount
      ) {
        dispatch({ type: UserActionType.OPEN_WARNING_PANEL });
      }
    } catch (error) {
      console.log((error as Error).message);
    }
  };

export const fetchNotifications = () => async (dispatch: DispatchType<UserAction>) => {
  try {
    const { data } = await request<NotificationType[]>({
      method: 'GET',
      url: '/getUserNotification'
    });
    dispatch({ type: UserActionType.FETCH_NOTIFICATION, payload: data });
  } catch (error) {
    console.log((error as Error).message);
  }
};

export const updateInvitation =
  (id: number, accept: boolean) => (dispatch: DispatchType<UserAction>) => {
    dispatch({
      type: UserActionType.UPDATE_NOTIFICATION,
      payload: {
        id,
        status: accept ? NotificationStatusEnum.ACCEPTED : NotificationStatusEnum.DECLINED
      }
    });
  };

export const fetchRoomList =
  (tab: 'my' | 'public') => async (dispatch: DispatchType<UserAction>) => {
    try {
      const url = tab === 'my' ? '/getMyChatrooms' : '/getPublicChatrooms';
      dispatch({ type: UserActionType.LOADING_CHATROOM });
      const { data } = await request<RoomListItemType[]>({
        method: 'GET',
        url
      });
      dispatch({ type: UserActionType.FETCH_CHATROOM_LIST, payload: data });
      dispatch({ type: UserActionType.SET_SIDEBAR_TAB, payload: tab });
    } catch (error) {
      console.log((error as Error).message);
    }
  };
