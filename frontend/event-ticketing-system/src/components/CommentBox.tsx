import { useState, useEffect } from 'react';
import { retrieveAllReviewsForEvent, deleteReview, updateReview, saveReview } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex.tsx';

interface Review {
  id: string;
  emailOfReviewer: string;
  comment: string;
  date: string;
  edited: boolean;
}

interface CommentBoxProps {
  eventId: string;
  updateEvent: () => void;
}

function CommentBox({ eventId, updateEvent }: CommentBoxProps): JSX.Element {
  const [reviews, setReviews] = useState<Review[]>([]);
  const [newComment, setNewComment] = useState('');
  const { isAuthenticated, email } = useAuth();

  useEffect(() => {
    retrieveAllReviewsForEvent(eventId)
      .then((response) => {
        console.log('API Response:', response.data);
        setReviews(response.data);
      })
      .catch((error) => {
        console.error('Error fetching reviews:', error);
      });
  }, [eventId]);

  function handleDeleteReview(reviewId: string): void {
    deleteReview(reviewId)
      .then(() => {
        retrieveAllReviewsForEvent(eventId)
          .then((response) => {
            setReviews(response.data);
            updateEvent();
          })
          .catch((error) => {
            console.error('Error fetching reviews:', error);
          });
      })
      .catch((error) => {
        console.error('Error deleting review:', error);
      });
  }

  function handleEditReview(reviewId: string): void {
    const updatedComment = prompt('Enter the updated comment:');
    if (updatedComment !== null) {
      const updatedReview = {
        comment: updatedComment,
      };

      updateReview(reviewId, updatedReview)
        .then(() => {
          retrieveAllReviewsForEvent(eventId)
            .then((response) => {
              setReviews(response.data);
              updateEvent();
            })
            .catch((error) => {
              console.error('Error fetching reviews:', error);
            });
        })
        .catch((error) => {
          console.error('Error updating review:', error);
        });
    }
  }

  function handleAddComment(): void {
    if (newComment.trim() === '') {
      alert('Please enter a comment.');
      return;
    }

    const reviewToAdd = {
      comment: newComment,
    };

    saveReview(eventId, reviewToAdd)
      .then(() => {
        retrieveAllReviewsForEvent(eventId)
          .then((response) => {
            setReviews(response.data);
            updateEvent();
            setNewComment('');
          })
          .catch((error) => {
            console.error('Error fetching reviews:', error);
          });
      })
      .catch((error) => {
        console.error('Error saving review:', error);
      });
  }

  return (
    <div className="comment-box">
      <h2>Comments</h2>
      {isAuthenticated ? (
        <div className="mb-3">
          <textarea
            className="form-control"
            placeholder="Add your comment..."
            rows={3}
            value={newComment}
            onChange={(e) => setNewComment(e.target.value)}
          />
          <button className="btn btn-primary mb-3" onClick={handleAddComment}>
            Add Comment
          </button>
        </div>
      ) : (
        <p>Please log in to add comments.</p>
      )}
      {reviews.map((review) => (
        <div key={review.id} className="comment border p-3 mb-3">
          <p className="mb-2">
            <strong>Email:</strong> {review.emailOfReviewer}
          </p>
          <p className="mb-2">
            <strong>Comment:</strong> {review.comment}
          </p>
          <p className="mb-2">
            <strong>Date:</strong> {review.date}
          </p>
          <p className="mb-2">
            <strong>Edited:</strong> {review.edited ? 'Yes' : 'No'}
          </p>
          {isAuthenticated && email === review.emailOfReviewer && (
            <div className="btn-group">
              <button className="btn btn-sm btn-info" onClick={() => handleEditReview(review.id)}>
                Edit
              </button>
              <button className="btn btn-sm btn-danger" onClick={() => handleDeleteReview(review.id)}>
                Delete
              </button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default CommentBox;
