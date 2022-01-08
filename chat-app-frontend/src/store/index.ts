import { applyMiddleware, createStore, combineReducers } from 'redux';
import thunk from 'redux-thunk';
import roomReducer from './roomReducer';
import userReducer from './userReducer';

const rootReducer = combineReducers({ user: userReducer, room: roomReducer });
const store = createStore(rootReducer, applyMiddleware(thunk));

export type RootState = ReturnType<typeof rootReducer>;
export default store;
