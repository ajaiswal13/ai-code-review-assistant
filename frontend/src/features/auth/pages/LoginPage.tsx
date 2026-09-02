import { useState, type SyntheticEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'

import { useAuth } from '../context/useAuth'

export function LoginPage() {
    const { login } = useAuth()
    const navigate = useNavigate()

    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    const handleSubmit = async (event: SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()

        setError('')
        setIsSubmitting(true)

        try {
            await login(email, password)
            navigate('/reviews')
        } catch {
            setError('Invalid email or password.')
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
            <div className="w-full max-w-md rounded-xl bg-white p-8 shadow-md">
                <div className="mb-8 text-center">
                    <h1 className="text-2xl font-bold text-gray-900">
                        AI Code Review Assistant
                    </h1>

                    <p className="mt-2 text-sm text-gray-600">
                        Sign in to review your code
                    </p>
                </div>

                <form onSubmit={handleSubmit} className="space-y-5">
                    <div>
                        <label
                            htmlFor="email"
                            className="mb-1 block text-sm font-medium text-gray-700"
                        >
                            Email
                        </label>

                        <input
                            id="email"
                            type="email"
                            value={email}
                            onChange={(event) => setEmail(event.target.value)}
                            required
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-gray-500"
                            placeholder="you@example.com"
                        />
                    </div>

                    <div>
                        <label
                            htmlFor="password"
                            className="mb-1 block text-sm font-medium text-gray-700"
                        >
                            Password
                        </label>

                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(event) => setPassword(event.target.value)}
                            required
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-gray-500"
                            placeholder="Enter your password"
                        />
                    </div>

                    {error && (
                        <p className="text-sm text-red-600" role="alert">
                            {error}
                        </p>
                    )}

                    <button
                        type="submit"
                        disabled={isSubmitting}
                        className="w-full rounded-lg bg-black px-4 py-2.5 font-medium text-white disabled:cursor-not-allowed disabled:opacity-50"
                    >
                        {isSubmitting ? 'Signing in...' : 'Sign in'}
                    </button>
                </form>

                <p className="mt-6 text-center text-sm text-gray-600">
                    Don't have an account?{' '}
                    <Link
                        to="/register"
                        className="font-medium text-gray-900 underline"
                    >
                        Create account
                    </Link>
                </p>
            </div>
        </div>
    )
}