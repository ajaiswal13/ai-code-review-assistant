import { Link, Outlet } from 'react-router-dom'

import { useAuth } from '../../features/auth/context/useAuth'

export default function AuthenticatedLayout() {
    const { logout } = useAuth()

    return (
        <div className="min-h-screen bg-gray-50">
            <header className="border-b border-gray-200 bg-white">
                <div className="mx-auto flex h-16 max-w-7xl items-center justify-between px-6">
                    <Link
                        to="/reviews"
                        className="text-lg font-bold tracking-tight text-gray-900"
                    >
                        AI Code Review Assistant
                    </Link>

                    <button
                        type="button"
                        onClick={logout}
                        className="rounded-lg px-3 py-2 text-sm font-medium text-gray-500 transition hover:bg-gray-100 hover:text-gray-900"
                    >
                        Logout
                    </button>
                </div>
            </header>

            <main>
                <Outlet />
            </main>
        </div>
    )
}