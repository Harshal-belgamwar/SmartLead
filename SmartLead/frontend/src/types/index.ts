export enum UserRole {
    ADMIN = 'admin',
    SALES = 'sales',
}

export interface User {
    id: string;
    name: string;
    email: string;
    role: UserRole;
}

export enum LeadStatus {
    NEW = 'New',
    CONTACTED = 'Contacted',
    QUALIFIED = 'Qualified',
    LOST = 'Lost',
}

export enum LeadSource {
    WEBSITE = 'Website',
    INSTAGRAM = 'Instagram',
    REFERRAL = 'Referral',
}

export interface Lead {
    _id: string;
    name: string;
    email: string;
    status: LeadStatus;
    source: LeadSource;
    createdAt: string;
}

export interface PaginationData {
    page: number;
    limit: number;
    total: number;
    pages: number;
}
