export type ReviewStatus =
    | 'PENDING'
    | 'PROCESSING'
    | 'COMPLETED'
    | 'FAILED'

export interface ReviewIssue {
    id: string
    severity: string
    category: string
    line: number | null
    message: string
    suggestion: string
}

export interface ReviewResult {
    id: string
    summary: string
    score: number
    issues: ReviewIssue[]
}

export interface Review {
    id: string
    language: string
    status: ReviewStatus
    createdAt: string
    updatedAt: string
    result: ReviewResult | null
}

export interface CreateReviewRequest {
    language: string
    code: string
}