import { notification } from 'antd';
import axios from 'axios';
import qs, { ParsedUrlQueryInput } from 'querystring';
import { RoomActionType, UserActionType } from '../@types/enums';
import store from '../store';

interface RequestParams<T> {
  method: 'GET' | 'POST' | 'DELETE';
  url: string;
  body?: T;
  header?: any;
}

interface TResponse<T> {
  errCode: number;
  data?: T;
  errMsg?: string;
}

// export const BASE_URL = `${location.protocol}//${location.hostname}`;
export const BASE_URL = 'https://chatapp-final-team-rice.herokuapp.com';

const service = axios.create({
  // baseURL: 'https://www.fastmock.site/mock/3cd19c6749c291f038e517cb20d82db8/api'
  baseURL: BASE_URL
});

service.interceptors.request.use((config) => {
  if (config.headers) {
    (config.headers['Content-Type'] = 'application/x-www-form-urlencoded'),
      (config.headers['Access-Control-Allow-Origin'] = '*');
    if (localStorage.getItem('token')) {
      config.headers['userId'] = localStorage.getItem('token')!;
    }
  }
  return config;
});

service.interceptors.response.use(
  (res: any) => {
    if (res.data.errCode !== 0) {
      notification.error({
        message: res.data.errMsg
      });
      throw new Error(`${res.data.errMsg}`);
    }

    return res;
  },
  (error) => {
    const { response } = error;

    if (!response) {
      notification.error({
        message: error.message
      });

      throw new Error(`${error.message}`);
    }

    if (response.status === 500) {
      notification.error({
        message: 'Server Error'
      });
    } else if (response.status === 401) {
      notification.error({
        message: response.data
      });
      store.dispatch({ type: UserActionType.USER_LOGOUT });
      store.dispatch({ type: RoomActionType.LEAVE_ROOM });
    } else {
      notification.error({
        message: response.data
      });
    }

    throw new Error(`${response.data}`);
  }
);

const request = async <ResponseType = any, RequestType = any>(
  params: RequestParams<RequestType>
): Promise<TResponse<ResponseType>> => {
  const response = await service.request<TResponse<ResponseType>>({
    method: params.method,
    url: params.url,
    data: params.method === 'GET' ? null : qs.stringify((params.body || {}) as ParsedUrlQueryInput),
    headers: params.header
  });

  return response.data;
};

export default request;
