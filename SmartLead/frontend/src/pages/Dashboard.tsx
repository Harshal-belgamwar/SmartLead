import React, { useState, useEffect, useCallback } from 'react';
import { useAuth } from '../context/AuthContext';
import client from '../api/client';
import { Lead, LeadStatus, LeadSource, PaginationData } from '../types';
import { Button } from '../components/ui/Button';
import { Card } from '../components/ui/Card';
import { LeadModal } from '../components/LeadModal';
import {
    LogOut,
    Search,
    Plus,
    Download,
    ChevronLeft,
    ChevronRight,
    Activity,
    Trash2,
    Edit2,
    Moon,
    Sun
} from 'lucide-react';
import { format } from 'date-fns';
import { cn } from '../components/ui/Button';

const Dashboard: React.FC = () => {
    const { user, logout } = useAuth();
    const [leads, setLeads] = useState<Lead[]>([]);
    const [pagination, setPagination] = useState<PaginationData | null>(null);
    const [isLoading, setIsLoading] = useState(true);
    const [isDarkMode, setIsDarkMode] = useState(localStorage.getItem('theme') === 'dark');

    // Filter States
    const [search, setSearch] = useState('');
    const [status, setStatus] = useState('');
    const [source, setSource] = useState('');
    const [sort, setSort] = useState('latest');
    const [page, setPage] = useState(1);

    // Modal State
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [editingLead, setEditingLead] = useState<Lead | undefined>(undefined);

    // Debounced search
    const [debouncedSearch, setDebouncedSearch] = useState('');

    useEffect(() => {
        const timer = setTimeout(() => {
            setDebouncedSearch(search);
            setPage(1);
        }, 500);
        return () => clearTimeout(timer);
    }, [search]);

    useEffect(() => {
        if (isDarkMode) {
            document.documentElement.classList.add('dark');
            localStorage.setItem('theme', 'dark');
        } else {
            document.documentElement.classList.remove('dark');
            localStorage.setItem('theme', 'light');
        }
    }, [isDarkMode]);

    const fetchLeads = useCallback(async () => {
        setIsLoading(true);
        try {
            const params = {
                search: debouncedSearch,
                status,
                source,
                sort,
                page,
                limit: 10
            };
            const response = await client.get('/leads', { params });
            setLeads(response.data.data.leads);
            setPagination(response.data.pagination);
        } catch (error) {
            console.error('Failed to fetch leads', error);
        } finally {
            setIsLoading(false);
        }
    }, [debouncedSearch, status, source, sort, page]);

    useEffect(() => {
        fetchLeads();
    }, [fetchLeads]);

    const handleCreateLead = async (data: any) => {
        try {
            await client.post('/leads', data);
            fetchLeads();
        } catch (error) {
            console.error('Failed to create lead', error);
        }
    };

    const handleUpdateLead = async (data: any) => {
        if (!editingLead) return;
        try {
            await client.patch(`/leads/${editingLead._id}`, data);
            fetchLeads();
        } catch (error) {
            console.error('Failed to update lead', error);
        }
    };

    const handleDeleteLead = async (id: string) => {
        if (!window.confirm('Are you sure you want to delete this lead?')) return;
        try {
            await client.delete(`/leads/${id}`);
            fetchLeads();
        } catch (error) {
            console.error('Failed to delete lead', error);
        }
    };

    const handleExportCSV = () => {
        const headers = ['Name', 'Email', 'Status', 'Source', 'Created At'];
        const rows = leads.map(lead => [
            lead.name,
            lead.email,
            lead.status,
            lead.source,
            format(new Date(lead.createdAt), 'yyyy-MM-dd')
        ]);

        const csvContent = "data:text/csv;charset=utf-8,"
            + headers.join(",") + "\n"
            + rows.map(e => e.join(",")).join("\n");

        const encodedUri = encodeURI(csvContent);
        const link = document.createElement("a");
        link.setAttribute("href", encodedUri);
        link.setAttribute("download", "leads_export.csv");
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
    };

    return (
        <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex flex-col transition-colors duration-200">
            <LeadModal
                isOpen={isModalOpen}
                onClose={() => {
                    setIsModalOpen(false);
                    setEditingLead(undefined);
                }}
                onSubmit={editingLead ? handleUpdateLead : handleCreateLead}
                initialData={editingLead}
                title={editingLead ? 'Edit Lead' : 'Add New Lead'}
            />

            {/* Header */}
            <header className="bg-white dark:bg-gray-800 border-b border-gray-200 dark:border-gray-700 px-6 py-4 sticky top-0 z-10">
                <div className="max-w-7xl mx-auto flex justify-between items-center">
                    <div className="flex items-center space-x-2">
                        <div className="w-10 h-10 bg-indigo-600 rounded-lg flex items-center justify-center text-white font-bold text-xl">SL</div>
                        <h1 className="text-xl font-bold text-gray-900 dark:text-white">SmartLead</h1>
                    </div>
                    <div className="flex items-center space-x-4">
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={() => setIsDarkMode(!isDarkMode)}
                            className="text-gray-500 dark:text-gray-400"
                        >
                            {isDarkMode ? <Sun className="w-5 h-5" /> : <Moon className="w-5 h-5" />}
                        </Button>
                        <div className="text-right hidden sm:block">
                            <p className="text-sm font-medium text-gray-900 dark:text-white">{user?.name}</p>
                            <p className="text-xs text-gray-500 dark:text-gray-400 capitalize">{user?.role}</p>
                        </div>
                        <Button variant="ghost" size="sm" onClick={logout} className="text-gray-500 hover:text-red-600 dark:text-gray-400 dark:hover:text-red-400">
                            <LogOut className="w-4 h-4 mr-2" />
                            Logout
                        </Button>
                    </div>
                </div>
            </header>

            {/* Main Content */}
            <main className="flex-1 max-w-7xl mx-auto w-full p-6 space-y-6">
                <div className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4">
                    <div>
                        <h2 className="text-2xl font-bold text-gray-900 dark:text-white">Leads Dashboard</h2>
                        <p className="text-gray-500 dark:text-gray-400 text-sm">Manage and track your customer leads</p>
                    </div>
                    <div className="flex space-x-2 w-full sm:w-auto">
                        <Button variant="outline" size="sm" onClick={handleExportCSV} className="dark:bg-gray-800 dark:text-white dark:border-gray-600 dark:hover:bg-gray-700">
                            <Download className="w-4 h-4 mr-2" />
                            Export CSV
                        </Button>
                        <Button size="sm" onClick={() => setIsModalOpen(true)}>
                            <Plus className="w-4 h-4 mr-2" />
                            Add Lead
                        </Button>
                    </div>
                </div>

                {/* Filters */}
                <Card className="p-4 dark:bg-gray-800 dark:border-gray-700">
                    <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                        <div className="relative">
                            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-4 h-4 text-gray-400" />
                            <input
                                type="text"
                                placeholder="Search name or email..."
                                className="pl-10 pr-3 py-2 w-full border border-gray-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white rounded-md text-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                                value={search}
                                onChange={(e) => setSearch(e.target.value)}
                            />
                        </div>

                        <select
                            className="px-3 py-2 border border-gray-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white rounded-md text-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                            value={status}
                            onChange={(e) => setStatus(e.target.value)}
                        >
                            <option value="">All Statuses</option>
                            {Object.values(LeadStatus).map(s => <option key={s} value={s}>{s}</option>)}
                        </select>

                        <select
                            className="px-3 py-2 border border-gray-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white rounded-md text-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                            value={source}
                            onChange={(e) => setSource(e.target.value)}
                        >
                            <option value="">All Sources</option>
                            {Object.values(LeadSource).map(s => <option key={s} value={s}>{s}</option>)}
                        </select>

                        <select
                            className="px-3 py-2 border border-gray-300 dark:border-gray-600 dark:bg-gray-700 dark:text-white rounded-md text-sm focus:ring-1 focus:ring-indigo-500 focus:border-indigo-500"
                            value={sort}
                            onChange={(e) => setSort(e.target.value)}
                        >
                            <option value="latest">Sort by Latest</option>
                            <option value="oldest">Sort by Oldest</option>
                        </select>
                    </div>
                </Card>

                {/* Table/List */}
                <Card className="overflow-hidden dark:bg-gray-800 dark:border-gray-700">
                    <div className="overflow-x-auto">
                        <table className="min-w-full divide-y divide-gray-200 dark:divide-gray-700 text-left">
                            <thead className="bg-gray-50 dark:bg-gray-900/50">
                                <tr>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">Lead Info</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">Status</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">Source</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider">Created</th>
                                    <th className="px-6 py-3 text-xs font-medium text-gray-500 dark:text-gray-400 uppercase tracking-wider text-right">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="bg-white dark:bg-gray-800 divide-y divide-gray-200 dark:divide-gray-700">
                                {isLoading ? (
                                    Array.from({ length: 5 }).map((_, i) => (
                                        <tr key={i} className="animate-pulse">
                                            <td colSpan={5} className="px-6 py-4 h-16">
                                                <div className="bg-gray-100 dark:bg-gray-700 h-full w-full rounded-md"></div>
                                            </td>
                                        </tr>
                                    ))
                                ) : leads.length === 0 ? (
                                    <tr>
                                        <td colSpan={5} className="px-6 py-12 text-center text-gray-500 dark:text-gray-400">
                                            <div className="flex flex-col items-center">
                                                <Activity className="w-12 h-12 text-gray-300 dark:text-gray-600 mb-2" />
                                                <p>No leads found matching your filters</p>
                                            </div>
                                        </td>
                                    </tr>
                                ) : (
                                    leads.map((lead) => (
                                        <tr key={lead._id} className="hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors">
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <div className="flex items-center">
                                                    <div className="w-8 h-8 rounded-full bg-indigo-100 dark:bg-indigo-900/30 text-indigo-600 dark:text-indigo-400 flex items-center justify-center font-semibold text-sm">
                                                        {lead.name.charAt(0)}
                                                    </div>
                                                    <div className="ml-3">
                                                        <div className="text-sm font-medium text-gray-900 dark:text-white">{lead.name}</div>
                                                        <div className="text-xs text-gray-500 dark:text-gray-400">{lead.email}</div>
                                                    </div>
                                                </div>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap">
                                                <span className={cn(
                                                    "px-2.5 py-0.5 rounded-full text-xs font-medium",
                                                    lead.status === LeadStatus.NEW && "bg-blue-100 text-blue-800 dark:bg-blue-900/40 dark:text-blue-300",
                                                    lead.status === LeadStatus.CONTACTED && "bg-yellow-100 text-yellow-800 dark:bg-yellow-900/40 dark:text-yellow-300",
                                                    lead.status === LeadStatus.QUALIFIED && "bg-green-100 text-green-800 dark:bg-green-900/40 dark:text-green-300",
                                                    lead.status === LeadStatus.LOST && "bg-red-100 text-red-800 dark:bg-red-900/40 dark:text-red-300"
                                                )}>
                                                    {lead.status}
                                                </span>
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                                                {lead.source}
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-500 dark:text-gray-400">
                                                {format(new Date(lead.createdAt), 'MMM d, yyyy')}
                                            </td>
                                            <td className="px-6 py-4 whitespace-nowrap text-right text-sm font-medium">
                                                <div className="flex justify-end space-x-2">
                                                    <button
                                                        onClick={() => {
                                                            setEditingLead(lead);
                                                            setIsModalOpen(true);
                                                        }}
                                                        className="text-gray-400 hover:text-indigo-600 dark:hover:text-indigo-400 p-1"
                                                    >
                                                        <Edit2 className="w-4 h-4" />
                                                    </button>
                                                    {user?.role === 'admin' && (
                                                        <button
                                                            onClick={() => handleDeleteLead(lead._id)}
                                                            className="text-gray-400 hover:text-red-500 dark:hover:text-red-400 p-1"
                                                        >
                                                            <Trash2 className="w-4 h-4" />
                                                        </button>
                                                    )}
                                                </div>
                                            </td>
                                        </tr>
                                    ))
                                )}
                            </tbody>
                        </table>
                    </div>

                    {/* Pagination */}
                    {pagination && pagination.pages > 1 && (
                        <div className="px-6 py-4 bg-gray-50 dark:bg-gray-900/30 border-t border-gray-200 dark:border-gray-700 flex items-center justify-between">
                            <div className="text-sm text-gray-500 dark:text-gray-400">
                                Showing <span className="font-medium text-gray-900 dark:text-white">{(page - 1) * 10 + 1}</span> to <span className="font-medium text-gray-900 dark:text-white">{Math.min(page * 10, pagination.total)}</span> of <span className="font-medium text-gray-900 dark:text-white">{pagination.total}</span> leads
                            </div>
                            <div className="flex space-x-2">
                                <Button
                                    variant="secondary"
                                    size="sm"
                                    disabled={page === 1}
                                    onClick={() => setPage(p => p - 1)}
                                    className="dark:bg-gray-700 dark:text-white dark:border-gray-600 dark:hover:bg-gray-600"
                                >
                                    <ChevronLeft className="w-4 h-4 mr-1" /> Prev
                                </Button>
                                <Button
                                    variant="secondary"
                                    size="sm"
                                    disabled={page === pagination.pages}
                                    onClick={() => setPage(p => p + 1)}
                                    className="dark:bg-gray-700 dark:text-white dark:border-gray-600 dark:hover:bg-gray-600"
                                >
                                    Next <ChevronRight className="w-4 h-4 ml-1" />
                                </Button>
                            </div>
                        </div>
                    )}
                </Card>
            </main>
        </div>
    );
};

export default Dashboard;
