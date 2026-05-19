import { Router } from 'express';
import {
    createLead,
    getAllLeads,
    getLead,
    updateLead,
    deleteLead,
} from '../controllers/leadController';
import { protect, restrictTo } from '../middleware/authMiddleware';
import { UserRole } from '../models/User';

const router = Router();

router.use(protect);

router
    .route('/')
    .get(getAllLeads)
    .post(createLead);

router
    .route('/:id')
    .get(getLead)
    .patch(updateLead)
    .delete(restrictTo(UserRole.ADMIN), deleteLead);

export default router;
