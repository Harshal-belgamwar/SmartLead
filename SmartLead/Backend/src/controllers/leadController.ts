import { Request, Response, NextFunction } from 'express';
import Lead, { LeadStatus, LeadSource } from '../models/Lead';
import { leadSchema } from '../utils/validation';
import { AppError } from '../middleware/errorHandler';

export const createLead = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const validatedData = leadSchema.parse(req.body);
        const newLead = await Lead.create(validatedData as any);

        res.status(201).json({
            status: 'success',
            data: { lead: newLead },
        });
    } catch (error: any) {
        next(error);
    }
};

export const getAllLeads = async (req: Request, res: Response, next: NextFunction) => {
    try {
        // 1. Filtering
        const { status, source, search, sort, page = '1', limit = '10' } = req.query;

        const queryObj: any = {};

        if (status) queryObj.status = status;
        if (source) queryObj.source = source;

        // 2. Search (Name or Email)
        if (search) {
            queryObj.$or = [
                { name: { $regex: search, $options: 'i' } },
                { email: { $regex: search, $options: 'i' } },
            ];
        }

        // 3. Sorting
        let sortBy = '-createdAt';
        if (sort === 'oldest') sortBy = 'createdAt';
        if (sort === 'latest') sortBy = '-createdAt';

        // 4. Pagination
        const pageNum = parseInt(page as string);
        const limitNum = parseInt(limit as string);
        const skip = (pageNum - 1) * limitNum;

        const leads = await Lead.find(queryObj)
            .sort(sortBy)
            .skip(skip)
            .limit(limitNum);

        const totalLeads = await Lead.countDocuments(queryObj);

        res.status(200).json({
            status: 'success',
            results: leads.length,
            pagination: {
                page: pageNum,
                limit: limitNum,
                total: totalLeads,
                pages: Math.ceil(totalLeads / limitNum),
            },
            data: { leads },
        });
    } catch (error: any) {
        next(error);
    }
};

export const getLead = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const lead = await Lead.findById(req.params.id);
        if (!lead) return next(new AppError('No lead found with that ID', 404));

        res.status(200).json({
            status: 'success',
            data: { lead },
        });
    } catch (error: any) {
        next(error);
    }
};

export const updateLead = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const validatedData = leadSchema.partial().parse(req.body);
        const lead = await Lead.findByIdAndUpdate(req.params.id, validatedData, {
            new: true,
            runValidators: true,
        });

        if (!lead) return next(new AppError('No lead found with that ID', 404));

        res.status(200).json({
            status: 'success',
            data: { lead },
        });
    } catch (error: any) {
        next(error);
    }
};

export const deleteLead = async (req: Request, res: Response, next: NextFunction) => {
    try {
        const lead = await Lead.findByIdAndDelete(req.params.id);
        if (!lead) return next(new AppError('No lead found with that ID', 404));

        res.status(204).json({
            status: 'success',
            data: null,
        });
    } catch (error: any) {
        next(error);
    }
};
