interface UserLoginRequest {
  username: string;
  password: string;
}

interface UserRegisterRequest {
  username: string;
  password: string;
  age: number;
  imageUrl: string;
  interests: string;
  school: string;
}

interface UserLoginResponse {
  data: UserType;
}

interface CreatRoomRequest {
  roomName: string;
  roomSize: number;
  isPrivate: boolean;
}

interface GetRoomResponse {
  chatRoomId: number;
  chatRoomName: string;
  isPrivate: boolean;
  messages: MessageType[];
  isAdmin: boolean;
}

interface SendMessageWebsocket {
  type: 'public' | 'private';
  content: string;
  senderId: number;
  receiverId: number;
  roomId: number;
}
