// useMusic.ts
import { useState } from "react";
import { v4 as uuidv4 } from "uuid";
import { musicAPI } from "../services/api";
import { Music } from "../types";

export const useMusicInternal = (songs: Music[]) => {
  const [currentSessionId, setCurrentSessionId] = useState<string | null>(null);
  const [currentMusicId, setCurrentMusicId] = useState<string | null>(null);
  const [isPlaying, setIsPlaying] = useState<boolean>(false);
  const [blobUrl, setBlobUrl] = useState<string | null>(null);

  const playMusic = async (musicId: string) => {
    if (currentSessionId) {
      await endCurrentSession();
    }

    const newSessionId = uuidv4();
    setCurrentSessionId(newSessionId);
    setCurrentMusicId(musicId);

    const blob = await musicAPI.getBlob(musicId, newSessionId);
    setBlobUrl(blob);
    setIsPlaying(true);
  };

  const endCurrentSession = async () => {
    if (currentSessionId) {
      await musicAPI.endStream(currentSessionId);
    }
    setIsPlaying(false);
  };

  const skipToNext = async () => {
    if (!currentMusicId) return;
    const currentIndex = songs.findIndex((s) => s.id === currentMusicId);
    const nextSong = songs[currentIndex + 1];
    if (nextSong) {
      await playMusic(nextSong.id);
    }
  };

  const skipToPrevious = async () => {
    if (!currentMusicId) return;
    const currentIndex = songs.findIndex((s) => s.id === currentMusicId);
    const prevSong = songs[currentIndex - 1];
    if (prevSong) {
      await playMusic(prevSong.id);
    }
  };

  const togglePlayPause = () => {
    setIsPlaying((prev) => !prev);
  };

  return {
    currentMusicId,
    isPlaying,
    blobUrl,
    playMusic,
    endCurrentSession,
    skipToNext,
    skipToPrevious,
    togglePlayPause,
  };
};
