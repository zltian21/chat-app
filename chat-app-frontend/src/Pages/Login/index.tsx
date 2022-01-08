import React, { useEffect } from 'react';
import { FaUserAlt, FaLock } from 'react-icons/fa';
import { RouteChildrenProps } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { Input, Form, Button } from 'antd';
import { useForm } from 'antd/lib/form/Form';

import Layout from '../../Components/Layout';
import { RootState } from '../../store';
import { userLogin } from '../../store/actions/user';
import './styles.scss';

export default function Login(props: RouteChildrenProps): JSX.Element {
  const [form] = useForm();
  const dispatch = useDispatch();
  const { isAuthenticated, isLoading } = useSelector((state: RootState) => state.user);

  useEffect(() => {
    if (isAuthenticated) {
      props.history.push('/main');
    }
  }, [isAuthenticated]);

  const handleSubmit = async () => {
    try {
      const result = await form.validateFields();
      if (result) {
        const fields = form.getFieldsValue();
        dispatch(userLogin(fields.username, fields.password));
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Layout>
      <div className="login">
        <div className="left"></div>
        <div className="right">
          <h1>LOGIN</h1>
          <Form form={form} requiredMark={false} className="form" onSubmitCapture={handleSubmit}>
            <Form.Item
              name="username"
              rules={[{ required: true, message: 'This field cannot be empty' }]}
            >
              <Input placeholder="Username" size="large" prefix={<FaUserAlt />} />
            </Form.Item>

            <Form.Item
              name="password"
              rules={[{ required: true, message: 'This field cannot be empty' }]}
            >
              <Input type="password" placeholder="Password" size="large" prefix={<FaLock />} />
            </Form.Item>

            <div style={{ marginTop: '3rem' }}>
              <Button
                type="primary"
                shape="round"
                htmlType="submit"
                size="large"
                style={{ minWidth: '8rem', height: '3rem' }}
                loading={isLoading}
              >
                Login
              </Button>
              <Button type="link" size="large" onClick={() => props.history.push('/register')}>
                Register
              </Button>
            </div>
          </Form>
        </div>
      </div>
    </Layout>
  );
}
