import React, { useEffect } from 'react'
import { useParams } from 'react-router-dom'

const ShortenUrlPage = () => {
    // useParams is hook used to get the slug from the url 
    const { url } = useParams();


    useEffect(() => {
        if (url) {
            window.location.href = import.meta.env.VITE_BACKEND_URL + `/${url}`;
        }
    }, [url]);
  return <p>Redirecting...</p>;
}

export default ShortenUrlPage