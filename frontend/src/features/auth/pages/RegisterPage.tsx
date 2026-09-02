import { useState, type SyntheticEvent } from 'react'
import { Link, useNavigate } from 'react-router-dom'

import { authApi } from '../api/authApi'

export function RegisterPage() {
    const navigate = useNavigate()

    const [firstName, setFirstName] = useState('')
    const [lastName, setLastName] = useState('')
    const [email, setEmail] = useState('')
    const [password, setPassword] = useState('')
    const [error, setError] = useState('')
    const [isSubmitting, setIsSubmitting] = useState(false)

    const handleSubmit = async (event: SyntheticEvent<HTMLFormElement>) => {
        event.preventDefault()

        setError('')
        setIsSubmitting(true)

        try {
            await authApi.register({
                firstName,
                lastName,
                email,
                password,
            })

            navigate('/login')
        } catch {
            setError('Unable to create your account. Please try again.')
        } finally {
            setIsSubmitting(false)
        }
    }

    return (
        <div className="flex min-h-screen items-center justify-center bg-gray-50 px-4">
            <div className="w-full max-w-md rounded-xl bg-white p-8 shadow-md">
                <div className="mb-8 text-center">
                    <h1 className="text-2xl font-bold text-gray-900">
                        Create your account
                    </h1>

                    <p className="mt-2 text-sm text-gray-600">
                        Start reviewing your code with AI
                    </p>
                </div>

                <form onSubmit={handleSubmit} className="space-y-5">
                    <div>
                        <label
                            htmlFor="firstName"
                            className="mb-1 block text-sm font-medium text-gray-700"
                        >
                            First name
                        </label>

                        <input
                            id="firstName"
                            type="text"
                            value={firstName}
                            onChange={(event) => setFirstName(event.target.value)}
                            required
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-gray-500"
                            placeholder="John"
                        />
                    </div>

                    <div>
                        <label
                            htmlFor="lastName"
                            className="mb-1 block text-sm font-medium text-gray-700"
                        >
                            Last name
                        </label>

                        <input
                            id="lastName"
                            type="text"
                            value={lastName}
                            onChange={(event) => setLastName(event.target.value)}
                            required
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-gray-500"
                            placeholder="Doe"
                        />
                    </div>

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
                            minLength={8}
                            className="w-full rounded-lg border border-gray-300 px-3 py-2 outline-none focus:border-gray-500"
                            placeholder="At least 8 characters"
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
                        {isSubmitting ? 'Creating account...' : 'Create account'}
                    </button>
                </form>

                <p className="mt-6 text-center text-sm text-gray-600">
                    Already have an account?{' '}
                    <Link
                        to="/login"
                        className="font-medium text-gray-900 underline"
                    >
                        Sign in
                    </Link>
                </p>
            </div>
        </div>
    )
}