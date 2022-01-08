import React, { useEffect, useState } from 'react';
import { FaUserAlt, FaLock, FaSchool } from 'react-icons/fa';
import { RouteChildrenProps } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { Input, Form, Button, Steps, Space, Avatar, Popover } from 'antd';
import {
  AppstoreOutlined,
  ArrowLeftOutlined,
  ArrowRightOutlined,
  SmileOutlined,
  UserOutlined
} from '@ant-design/icons';
import { useForm } from 'antd/lib/form/Form';

import Layout from '../../Components/Layout';
import { RootState } from '../../store';
import { userRegister } from '../../store/actions/user';

import './styles.scss';

const AVATARS = [
  'jed',
  'joe',
  'jack',
  'jess',
  'jodi',
  'jacques',
  'jordan',
  'josephine',
  'jude',
  'jon',
  'jazabelle',
  'julie',
  'jean',
  'jabala',
  'jaqueline',
  'jenni',
  'jeri',
  'jia',
  'jai',
  'jolee',
  'jane',
  'josh',
  'jana',
  'jeane',
  'jerry',
  'jake',
  'james',
  'jocelyn'
];

export default function Register(props: RouteChildrenProps): JSX.Element {
  const dispatch = useDispatch();
  const { isAuthenticated, isLoading } = useSelector((state: RootState) => state.user);
  const [form] = useForm();

  const [form1, setForm1] = useState({});
  const [step, setStep] = useState<number>(0);
  const [selectedAvatar, setSelectedAvatar] = useState<string>('');
  const [avatarSelectVisible, setAvatarSelectVisible] = useState<boolean>(false);

  useEffect(() => {
    if (isAuthenticated) {
      props.history.push('/main');
    }
  }, [isAuthenticated]);

  const handleMoveStep = async (incre: number) => {
    const result = await form.validateFields();
    if (incre === 1) {
      setForm1(form.getFieldsValue());
    }
    if (result) {
      setStep(step + incre);
    }
  };

  const handleSubmit = async () => {
    const result = await form.validateFields();
    if (result) {
      const formValues = form.getFieldsValue();
      formValues.age = +formValues.age;
      dispatch(userRegister(Object.assign({}, formValues, form1)));
    }
  };

  const renderStep1 = () => (
    <>
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

      <Form.Item
        name="confirm"
        rules={[
          { required: true, message: 'This field cannot be empty' },
          ({ getFieldValue }) => ({
            validator(_, value) {
              if (!value || getFieldValue('password') === value) {
                return Promise.resolve();
              }
              return Promise.reject(new Error('The two passwords that you entered do not match!'));
            }
          })
        ]}
      >
        <Input type="password" placeholder="Confirm" size="large" prefix={<FaLock />} />
      </Form.Item>

      <div style={{ marginTop: '3rem' }}>
        <Button
          type="primary"
          shape="round"
          size="large"
          icon={<ArrowRightOutlined style={{ marginRight: '1rem' }} />}
          style={{ width: '8rem', height: '3rem' }}
          onClick={() => handleMoveStep(1)}
        >
          Next
        </Button>
        <Button type="link" size="large" onClick={() => props.history.push('/login')}>
          Login
        </Button>
      </div>
    </>
  );

  const renderStep2 = () => (
    <>
      <Form.Item
        name="imageUrl"
        rules={[
          { required: true, message: 'This field cannot be empty' },
          { type: 'url', message: 'Invalid Url' }
        ]}
      >
        <>
          <Input type="hidden" />

          <Popover
            placement="bottomRight"
            visible={avatarSelectVisible}
            content={
              <>
                {AVATARS.map((each) => (
                  <span
                    key={each}
                    onClick={() => {
                      form.setFieldsValue({ imageUrl: `https://joeschmoe.io/api/v1/${each}` });
                      setSelectedAvatar(`https://joeschmoe.io/api/v1/${each}`);
                      setAvatarSelectVisible(false);
                    }}
                  >
                    <Avatar src={`https://joeschmoe.io/api/v1/${each}`} />
                  </span>
                ))}
              </>
            }
          >
            <div style={{ display: 'inline-block' }} onClick={() => setAvatarSelectVisible(true)}>
              <Avatar className="avatar" size={60} src={selectedAvatar} icon={<UserOutlined />} />
            </div>
          </Popover>
        </>
        {/* <Select placeholder="Select your avatar" size="large">
          {AVATARS.map((each) => (
            <Select.Option key={each} value={`https://joeschmoe.io/api/v1/${each}`}>
              <Avatar src={`https://joeschmoe.io/api/v1/${each}`} />
            </Select.Option>
          ))}
        </Select> */}
        {/* <Input type="url" placeholder="Avatar Url" size="large" prefix={<PictureOutlined />} /> */}
      </Form.Item>

      <Form.Item
        name="age"
        rules={[
          { required: true, message: 'This field cannot be empty' },
          {
            validator(_, value) {
              if (+value < 10 || +value > 70) {
                return Promise.reject(new Error('Invalid age'));
              }
              return Promise.resolve();
            }
          }
        ]}
      >
        <Input
          type="number"
          max={70}
          min={10}
          placeholder="Age"
          size="large"
          prefix={<SmileOutlined />}
        />
      </Form.Item>

      <Form.Item
        name="interests"
        rules={[{ required: true, message: 'This field cannot be empty' }]}
      >
        <Input placeholder="Interests" size="large" prefix={<AppstoreOutlined />} />
      </Form.Item>

      <Form.Item name="school" rules={[{ required: true, message: 'This field cannot be empty' }]}>
        <Input placeholder="School Name" size="large" prefix={<FaSchool />} />
      </Form.Item>

      <Space style={{ marginTop: '3rem' }}>
        <Button
          type="ghost"
          shape="round"
          size="large"
          style={{ width: '8rem', height: '3rem' }}
          icon={<ArrowLeftOutlined style={{ marginRight: '1rem' }} />}
          onClick={() => handleMoveStep(-1)}
        >
          Back
        </Button>
        <Button
          type="primary"
          shape="round"
          size="large"
          style={{ minWidth: '8rem', height: '3rem' }}
          onClick={() => handleSubmit()}
          loading={isLoading}
        >
          Register
        </Button>
      </Space>
    </>
  );

  const steps = [renderStep1(), renderStep2()];

  return (
    <Layout>
      <div className="register">
        <div className="left">
          <h1>Register</h1>

          <Steps direction="vertical" current={step}>
            <Steps.Step title="Create Username and Password" />
            <Steps.Step title="Choose Avatar and enter info" />
          </Steps>

          <Form form={form} key={0} requiredMark={false} className="form">
            {steps[step]}
          </Form>
        </div>
        <div className="right"></div>
      </div>
    </Layout>
  );
}
