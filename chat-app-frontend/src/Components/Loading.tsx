import React from 'react';
import { Spin } from 'antd';

export const Loading = (): JSX.Element => {
  return (
    <div
      style={{
        position: 'absolute',
        width: '100%',
        height: '100%',
        zIndex: 100,
        backgroundColor: 'rgba(255, 255, 255, 0.3)'
      }}
    >
      <Spin
        style={{
          position: 'absolute',
          top: '50%',
          left: '50%',
          transform: 'translate(-50%, -50%)'
        }}
      />
    </div>
  );
};
