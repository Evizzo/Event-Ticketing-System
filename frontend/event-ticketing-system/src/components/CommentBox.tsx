import { useState, useEffect } from 'react';
import { retrieveAllCommentsForEvent, deleteComment, updateComment, saveComment, likeComment, dislikeComment } from '../api/ApiService.ts';
import { useAuth } from '../api/AuthContex.tsx';

interface Comment {
  id: string;
  emailOfCommenter: string;
  comment: string;
  date: string;
  edited: boolean;
  likes: number;
  dislikes: number;
}

interface CommentBoxProps {
  eventId: string;
  updateEvent: () => void;
  commentCount: number;
}

type SortCriteria = 'date' | 'likes' | 'dislikes';

function CommentBox({ eventId, updateEvent, commentCount }: CommentBoxProps): JSX.Element {
  const [comments, setComments] = useState<Comment[]>([]);
  const [newComment, setNewComment] = useState('');
  const { isAuthenticated, email } = useAuth();
  const [message,setMessage] = useState("")
  const [commentCountFront, setCommentCountFront] = useState(commentCount)
  const [sortCriteria, setSortCriteria] = useState<SortCriteria>('date');

  useEffect(() => {
    retrieveAllCommentsForEvent(eventId, sortCriteria)
      .then((response) => {
        console.log('API Response:', response.data);
        setComments(response.data);
      })
      .catch((error) => {
        console.error('Error fetching comments:', error);
      });
  }, [eventId, sortCriteria]);

  function handleDeleteComment(commentId: string): void {
    deleteComment(commentId)
      .then(() => {
        retrieveAllCommentsForEvent(eventId, sortCriteria)
          .then((response) => {
            setCommentCountFront(commentCountFront-1)
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
          retrieveAllCommentsForEvent(eventId, sortCriteria)
            .then((response) => {
              setMessage("");
              setComments(response.data);
              updateEvent();
            })
            .catch((error) => {
              console.error('Error fetching comments:', error);
            });
        })
        .catch((error) => {
          setMessage(error.response.data.message)
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
        retrieveAllCommentsForEvent(eventId, sortCriteria)
          .then((response) => {
            setCommentCountFront(commentCountFront+1)

            setMessage("");
            setComments(response.data);
            updateEvent();
            setNewComment('');
          })
          .catch((error) => {
            console.error('Error fetching comments:', error);
          });
      })
      .catch((error) => {
        setMessage(error.response.data.message)
      });
  }
  function handleLikeComment(commentId: string): void {
    likeComment(commentId)
      .then(() => {
        retrieveAllCommentsForEvent(eventId, sortCriteria)
          .then((response) => {
            setComments(response.data);
            updateEvent();
          })
          .catch((error) => {
            console.error('Error fetching comments:', error);
          });
      })
      .catch((error) => {
        console.error('Error liking comment:', error);
      });
  }

  function handleDislikeComment(commentId: string): void {
    dislikeComment(commentId)
      .then(() => {
        retrieveAllCommentsForEvent(eventId, sortCriteria)
          .then((response) => {
            setComments(response.data);
            updateEvent();
          })
          .catch((error) => {
            console.error('Error fetching comments:', error);
          });
      })
      .catch((error) => {
        console.error('Error disliking comment:', error);
      });
  }

  const handleSortChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setSortCriteria(event.target.value as SortCriteria);
  };
  
  return (
    <div className="comment-box">
      <h2>Comments ({commentCountFront})</h2>
      <div className="row mb-3">
        <label htmlFor="sortCriteria" className="form-label me-2">
          Sort By:
        </label>
        <select
          id="sortCriteria"
          className="form-select"
          value={sortCriteria}
          onChange={handleSortChange}
          style={{ width: '120px', marginLeft: '12px' }} 
        >
          <option value="date">Date</option>
          <option value="likes">Likes</option>
          <option value="dislikes">Dislikes</option>
        </select>
      </div>
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
      {message && <div className="alert alert-warning">{message}</div>}
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
          <p className="mb-2">
             <span style={{ color: 'green' }}><strong>Likes:</strong> {comment.likes}</span>
          </p>
          <p className="mb-2">
            <span style={{ color: 'red' }}><strong>Dislikes:</strong> {comment.dislikes}</span>
          </p>
          {isAuthenticated && (
            <div className="btn-group">
              <button className="btn btn-sm btn-success" onClick={() => handleLikeComment(comment.id)}>
                Like
              </button>
              <button className="btn btn-sm btn-danger" onClick={() => handleDislikeComment(comment.id)}>
                Dislike
              </button>
            </div>
          )}
        </div>
      ))}
    </div>
  );
}

export default CommentBox;
