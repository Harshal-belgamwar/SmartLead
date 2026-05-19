import { Request, Response, NextFunction } from 'express';
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';
import User from '../models/User';
import { registerSchema, loginSchema } from '../utils/validation';
import { AppError } from '../middleware/errorHandler';

// Utility to create token
const signToken = (id: string) => {
    return jwt.sign({ id }, process.env.JWT_SECRET as string, {
        expiresIn: '1d',
    });
};

export const signup = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const validatedData = registerSchema.parse(req.body);

        const existingUser = await User.findOne({ email: validatedData.email });
        if (existingUser) {
            return next(new AppError('User already exists', 400));
        }

        const hashedPassword = await bcrypt.hash(validatedData.password, 12);

        const newUser = await User.create({
            ...validatedData,
            password: hashedPassword,
        } as any);

        const token = signToken(((newUser as any)._id).toString());

        res.status(201).json({
            status: 'success',
            token,
            data: {
                user: {
                    id: (newUser as any)._id,
                    name: (newUser as any).name,
                    email: (newUser as any).email,
                    role: (newUser as any).role,
                },
            },
        });
    } catch (error: any) {
        next(error);
    }
};

export const login = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const validatedData = loginSchema.parse(req.body);

        const user = await User.findOne({ email: validatedData.email });
        if (!user || !(await bcrypt.compare(validatedData.password, user.password))) {
            return next(new AppError('Invalid email or password', 401));
        }

        const token = signToken((user._id as any).toString());

        res.status(200).json({
            status: 'success',
            token,
            data: {
                user: {
                    id: user._id,
                    name: user.name,
                    email: user.email,
                    role: user.role,
                },
            },
        });
    } catch (error: any) {
        next(error);
    }
};
