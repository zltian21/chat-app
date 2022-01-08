import React, { ReactChild } from 'react';

import './styles.scss';

export default function Layout(props: { children: ReactChild }): JSX.Element {
  return (
    <div className="layout">
      <div className="child-container">
        <div className="heading">
          <div className="apple-ops">
            <span></span>
            <span></span>
            <span></span>
          </div>
          <span className="title">Chat App by team-Rice</span>
        </div>
        {props.children}
      </div>
    </div>
  );
}
