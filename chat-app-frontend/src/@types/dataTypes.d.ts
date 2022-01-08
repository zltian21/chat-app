interface UserType {
  id: number;
  username: string;
  imageUrl: string;
  hateSpeechCount: number;
  interests: string;
  schoolName: string;
  age: number;
}

interface RoomUserType {
  userId: number;
  userName: string;
  userAvatar: string;
}

interface MessageType {
  messageId: number;
  chatRoomId?: number;
  content: string;
  sender: RoomUserType | null;
  receiver?: RoomUserType | null;
  isPrivate: boolean;
  type?: 'new' | 'edit' | 'remove' | 'system';
}

interface NotificationType {
  id: number;
  content: string;
  type: 'invite' | 'message';
  status?: import('./enums').StatusEnum;
}

interface RoomListItemType {
  chatRoomId: number;
  chatRoomName: string;
  isPrivate: boolean;
  hasNewMessage: boolean;
}
