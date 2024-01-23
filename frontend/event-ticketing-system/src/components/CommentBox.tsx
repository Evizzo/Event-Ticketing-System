import { useState, useEffect } from 'react';
import { retrieveAllCommentsForEventByDate, deleteComment, updateComment, saveComment } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex.tsx';

interface Comment {
  id: string;
  emailOfCommenter: string;
  comment: string;
  date: string;
  edited: boolean;
}

interface CommentBoxProps {
  eventId: string;
  updateEvent: () => void;
}

function CommentBox({ eventId, updateEvent }: CommentBoxProps): JSX.Element {
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState('');
  const { isAuthenticated, email } = useAuth();

  useEffect(() => {
    retrieveAllCommentsForEventByDate(eventId)
      .then((response) => {
        console.log('API Response:', response.data);
        setComments(response.data);
      })
      .catch((error) => {
        console.error('Error fetching comments:', error);
      });
  }, [eventId]);

  function handleDeleteComment(commentId: string): void {
    deleteComment(commentId)
      .then(() => {
        retrieveAllCommentsForEventByDate(eventId)
          .then((response) => {
            setComments(response.data);
            updateEvent();
          })
          .catch((error) => {
            console.error('Error fetching comments:', error);
          });
      })
      .catch((error) => {
        console.error('Error deleting comments:', error);
      });
  }

  function handleEditComment(commentId: string): void {
    const updatedComment = prompt('Enter the updated comment:');
    if (updatedComment !== null) {
      const updatedCommentObject = {
        comment: updatedComment,
      };

      updateComment(commentId, updatedCommentObject)
        .then(() => {
          retrieveAllCommentsForEventByDate(eventId)
            .then((response) => {
              setComments(response.data);
              updateEvent();
            })
            .catch((error) => {
              console.error('Error fetching comments:', error);
            });
        })
        .catch((error) => {
          console.error('Error updating comment:', error);
        });
    }
  }

  function handleAddComment(): void {
    if (newComment.trim() === '') {
      alert('Please enter a comment.');
      return;
    }

    const commentToAdd = {
      comment: newComment,
    };

    saveComment(eventId, commentToAdd)
      .then(() => {
        retrieveAllCommentsForEventByDate(eventId)
          .then((response) => {
            setComments(response.data);
            updateEvent();
            setNewComment('');
          })
          .catch((error) => {
            console.error('Error fetching comments:', error);
          });
      })
      .catch((error) => {
        console.error('Error saving comments:', error);
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
      {comments.map((comment) => (
        <div key={comment.id} className="comment border p-3 mb-3">
          <p className="mb-2">
            <strong>Email:</strong> {comment.emailOfCommenter}
          </p>
          <p className="mb-2">
            <strong>Comment:</strong> {comment.comment}
          </p>
          <p className="mb-2">
            <strong>Date:</strong> {comment.date}
          </p>
          <p className="mb-2">
            <strong>Edited:</strong> {comment.edited ? 'Yes' : 'No'}
          </p>
          {isAuthenticated && email === comment.emailOfCommenter && (
            <div className="btn-group">
              <button className="btn btn-sm btn-info" onClick={() => handleEditComment(comment.id)}>
                Edit
              </button>
              <button className="btn btn-sm btn-danger" onClick={() => handleDeleteComment(comment.id)}>
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
