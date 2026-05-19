import React from 'react';
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';
import { Lead, LeadStatus, LeadSource } from '../types';
import { Button } from './ui/Button';
import { Input } from './ui/Input';
import { X } from 'lucide-react';

const leadSchema = z.object({
    name: z.string().min(2, 'Name must be at least 2 characters'),
    email: z.string().email('Invalid email address'),
    status: z.nativeEnum(LeadStatus),
    source: z.nativeEnum(LeadSource),
});

type LeadFormValues = z.infer<typeof leadSchema>;

interface LeadModalProps {
    isOpen: boolean;
    onClose: () => void;
    onSubmit: (data: LeadFormValues) => Promise<void>;
    initialData?: Lead;
    title: string;
}

export const LeadModal: React.FC<LeadModalProps> = ({
    isOpen,
    onClose,
    onSubmit,
    initialData,
    title
}) => {
    const {
        register,
        handleSubmit,
        formState: { errors, isSubmitting },
        reset
    } = useForm<LeadFormValues>({
        resolver: zodResolver(leadSchema),
        defaultValues: initialData ? {
            name: initialData.name,
            email: initialData.email,
            status: initialData.status,
            source: initialData.source
        } : {
            status: LeadStatus.NEW,
            source: LeadSource.WEBSITE
        }
    });

    if (!isOpen) return null;

    const handleFormSubmit = async (data: LeadFormValues) => {
        await onSubmit(data);
        reset();
        onClose();
    };

    return (
        <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-sm">
            <div className="bg-white rounded-xl shadow-xl max-w-md w-full overflow-hidden">
                <div className="px-6 py-4 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                    <h3 className="text-lg font-semibold text-gray-900">{title}</h3>
                    <button onClick={onClose} className="text-gray-400 hover:text-gray-600">
                        <X className="w-5 h-5" />
                    </button>
                </div>

                <form onSubmit={handleSubmit(handleFormSubmit)} className="p-6 space-y-4">
                    <Input
                        label="Full Name"
                        placeholder="Lead Name"
                        error={errors.name?.message}
                        {...register('name')}
                    />
                    <Input
                        label="Email Address"
                        type="email"
                        placeholder="lead@example.com"
                        error={errors.email?.message}
                        {...register('email')}
                    />

                    <div className="grid grid-cols-2 gap-4">
                        <div className="space-y-1">
                            <label className="block text-sm font-medium text-gray-700">Status</label>
                            <select
                                {...register('status')}
                                className="block w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:ring-indigo-500 focus:border-indigo-500"
                            >
                                {Object.values(LeadStatus).map(s => <option key={s} value={s}>{s}</option>)}
                            </select>
                        </div>

                        <div className="space-y-1">
                            <label className="block text-sm font-medium text-gray-700">Source</label>
                            <select
                                {...register('source')}
                                className="block w-full rounded-md border border-gray-300 px-3 py-2 text-sm focus:ring-indigo-500 focus:border-indigo-500"
                            >
                                {Object.values(LeadSource).map(s => <option key={s} value={s}>{s}</option>)}
                            </select>
                        </div>
                    </div>

                    <div className="pt-4 flex space-x-3">
                        <Button variant="secondary" className="flex-1" onClick={onClose}>
                            Cancel
                        </Button>
                        <Button type="submit" className="flex-1" isLoading={isSubmitting}>
                            {initialData ? 'Update' : 'Create'} Lead
                        </Button>
                    </div>
                </form>
            </div>
        </div>
    );
};
