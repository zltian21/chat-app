import React, { useState } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { Redirect, RouteChildrenProps } from 'react-router';
import { Spin } from 'antd';

import Layout from '../../Components/Layout';
import Sidebar from './Sidebar';
import ChatPanel from './ChatPanel';
import { RoomsList } from './RoomsList';
import RoomDrawer from './RoomDrawer';
import { RootState } from '../../store';
import { fetchRoomUsers } from '../../store/actions/room';

import './styles.scss';

export default function Main(props: RouteChildrenProps): JSX.Element {
  const dispatch = useDispatch();
  const { isAuthenticated, user } = useSelector((state: RootState) => state.user);
  const { currentRoom } = useSelector((state: RootState) => state.room);
  const [drawerVisible, setDrawerVisible] = useState<boolean>(false);

  if (!isAuthenticated) {
    return <Redirect to="/login" />;
  }

  if (!user) {
    return (
      <Layout>
        <Spin
          size="large"
          style={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)'
          }}
        />
      </Layout>
    );
  }

  return (
    <Layout>
      <div className="main">
        <Sidebar {...props} />
        <RoomsList />
        <ChatPanel
          openDrawer={() => {
            dispatch(fetchRoomUsers(currentRoom!.chatRoomId));
            setDrawerVisible(true);
          }}
        />

        <RoomDrawer visible={drawerVisible} onClose={() => setDrawerVisible(false)} />
      </div>
    </Layout>
  );
}
