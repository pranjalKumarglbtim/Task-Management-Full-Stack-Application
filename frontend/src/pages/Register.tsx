import React from 'react';
import { useForm } from 'react-hook-form';
import { Button } from '../components/Button';
import { Input } from '../components/Input';
import api from '../api/axios';
import { useNavigate, Link } from 'react-router-dom';
import toast from 'react-hot-toast';

interface RegisterForm {
  username: string;
  email: string;
  password: string;
}

export default function Register() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm<RegisterForm>();

  const navigate = useNavigate();

  const onSubmit = async (data: RegisterForm) => {
    try {
      console.log('Registering with:', data);
      const response = await api.post('/auth/register', data);
      console.log('Registration response:', response.data);
      toast.success('Registration successful! Please login.');
      navigate('/login');
    } catch (error: any) {
      console.error('Registration error:', error);
      const message = error.response?.data?.message || error.message || 'Registration failed';
      toast.error(message);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-100 dark:bg-gray-900">
      <div className="card w-full max-w-md">
        <h2 className="text-2xl font-bold mb-6 text-center">
          Create Account
        </h2>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input
            label="Username"
            {...register('username', {
              required: 'Username is required',
              minLength: {
                value: 3,
                message: 'Username must be at least 3 characters'
              }
            })}
            error={errors.username?.message}
          />

          <Input
            label="Email"
            type="email"
            {...register('email', {
              required: 'Email is required'
            })}
            error={errors.email?.message}
          />

          <Input
            label="Password"
            type="password"
            {...register('password', {
              required: 'Password is required',
              minLength: {
                value: 8,
                message: 'Password must be at least 8 characters'
              }
            })}
            error={errors.password?.message}
          />

          <Button type="submit" className="w-full">
            Register
          </Button>
        </form>

        <p className="mt-4 text-center">
          Already have an account?{' '}
          <Link
            to="/login"
            className="text-primary-600 hover:underline"
          >
            Login
          </Link>
        </p>
      </div>
    </div>
  );
}