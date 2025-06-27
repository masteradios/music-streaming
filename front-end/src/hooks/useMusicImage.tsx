import { useState, useEffect } from "react";
import { musicAPI } from "../services/api";

export const useMusicImage = (musicId: string) => {
  const [imageUrl, setImageUrl] = useState<string | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(false);

  useEffect(() => {
    let isMounted = true;
    let objectUrl: string | null = null;

    const fetchImage = async () => {
      try {
        setLoading(true);
        setError(false);
        objectUrl = await musicAPI.getMusicImage(musicId);

        if (isMounted) {
          setImageUrl(objectUrl);
        }
      } catch (err) {
        if (isMounted) {
          setError(true);
        }
      } finally {
        if (isMounted) {
          setLoading(false);
        }
      }
    };

    fetchImage();

    return () => {
      isMounted = false;
      if (objectUrl) {
        URL.revokeObjectURL(objectUrl);
      }
    };
  }, [musicId]);

  useEffect(() => {
    return () => {
      if (imageUrl) {
        URL.revokeObjectURL(imageUrl);
      }
    };
  }, [imageUrl]);

  return { imageUrl, loading, error };
};
