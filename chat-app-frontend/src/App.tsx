import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { HashRouter as Router, Route, Redirect } from 'react-router-dom';

import Login from './Pages/Login';
import Main from './Pages/Main';
import Register from './Pages/Register';
import { RootState } from './store';
import { fetchUserInfo } from './store/actions/user';

function App(): JSX.Element {
  const dispatch = useDispatch();

  const user = useSelector((state: RootState) => state.user.user);
  const isAuthenticated = useSelector((state: RootState) => state.user.isAuthenticated);

  const token = useSelector((state: RootState) => state.user.token);

  useEffect(() => {
    if (token && !user) {
      dispatch(fetchUserInfo(token));
    }
  }, []);

  return (
    <Router hashType="slash">
      <Route path="/*">
        {isAuthenticated ? <Redirect to="/main" /> : <Redirect to="/login" />}
      </Route>
      <Route path="/login" exact component={Login} />
      <Route path="/register" exact component={Register} />
      <Route path="/main" exact component={Main} />
    </Router>
  );
}

export default App;
