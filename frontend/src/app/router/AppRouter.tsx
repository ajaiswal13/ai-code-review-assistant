import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { LoginPage } from '../../features/auth/pages/LoginPage'
import { RegisterPage } from '../../features/auth/pages/RegisterPage'
import ReviewsPage from '../../features/reviews/pages/ReviewsPage'
import CreateReviewPage from '../../features/reviews/pages/CreateReviewPage'
import ReviewDetailPage from '../../features/reviews/pages/ReviewDetailPage'
import { ProtectedRoute } from '../../features/auth/routes/ProtectedRoute'

function AppRouter() {
    return (
        <BrowserRouter>
            <Routes>
                <Route path="/login" element={<LoginPage />} />
                <Route path="/register" element={<RegisterPage />} />

                <Route element={<ProtectedRoute />}>
                    <Route path="/reviews" element={<ReviewsPage />} />
                    <Route path="/reviews/new" element={<CreateReviewPage />} />
                    <Route path="/reviews/:reviewId" element={<ReviewDetailPage />}
                    />
                </Route>
            </Routes>
        </BrowserRouter>
    )
}

export default AppRouter