import React, { useEffect, useRef, useState } from "react";
import {
  Play,
  Pause,
  SkipBack,
  SkipForward,
  Volume1,
  Volume2,
  Music,
} from "lucide-react";
import { useMusicImage } from "../hooks/useMusicImage";

interface MusicPlayerProps {
  currentMusic: {
    id: string;
    musicName: string;
    artistName: string;
    musicThumbnailUrl: string;
  } | null;
  isPlaying: boolean;
  onPlayPause: () => void;
  onSkipNext: () => void;
  onSkipPrevious: () => void;
  blobUrl: string | null;
}

export const MusicPlayer: React.FC<MusicPlayerProps> = ({
  currentMusic,
  isPlaying,
  onPlayPause,
  onSkipNext,
  onSkipPrevious,
  blobUrl,
}) => {
  const { imageUrl, loading, error } = useMusicImage(currentMusic?.id || "");
  const audioRef = useRef<HTMLAudioElement | null>(null);

  const [currentTime, setCurrentTime] = useState(0);
  const [duration, setDuration] = useState(0);
  const [isSeeking, setIsSeeking] = useState(false);
  const [volume, setVolume] = useState(1);

  // Handle play/pause and blob changes
  useEffect(() => {
    const audio = audioRef.current;
    if (!audio || !blobUrl) return;

    if (audio.src !== blobUrl) {
      audio.src = blobUrl;
    }

    if (isPlaying) {
      audio.play().catch((err) => console.error("Audio play failed:", err));
    } else {
      audio.pause();
    }
  }, [blobUrl, isPlaying]);

  // Track current time and duration
  useEffect(() => {
    const audio = audioRef.current;
    if (!audio) return;

    const updateTime = () => {
      if (!isSeeking) setCurrentTime(audio.currentTime);
    };

    const updateDuration = () => {
      setDuration(audio.duration);
    };

    audio.addEventListener("timeupdate", updateTime);
    audio.addEventListener("loadedmetadata", updateDuration);

    return () => {
      audio.removeEventListener("timeupdate", updateTime);
      audio.removeEventListener("loadedmetadata", updateDuration);
    };
  }, [isSeeking]);

  // Volume syncing
  useEffect(() => {
    if (audioRef.current) {
      audioRef.current.volume = volume;
    }
  }, [volume]);

  const handleSeek = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newTime = parseFloat(e.target.value);
    setCurrentTime(newTime);
    if (audioRef.current) {
      audioRef.current.currentTime = newTime;
    }
  };

  if (!currentMusic) return null;

  return (
    <>
      {/* Seek Slider */}
      <div className="fixed bottom-[58px] left-0 right-0 z-50">
        <input
          type="range"
          min={0}
          max={duration || 0}
          value={currentTime}
          onChange={handleSeek}
          onMouseDown={() => setIsSeeking(true)}
          onMouseUp={() => setIsSeeking(false)}
          step={0.1}
          className="w-full h-1 rounded-full appearance-none cursor-pointer"
          style={{
            background: `linear-gradient(to right, #a855f7 ${
              (currentTime / duration) * 100
            }%, #4b5563 ${(currentTime / duration) * 100}%)`,
          }}
        />
      </div>

      {/* Bottom Player Bar */}
      <div className="fixed bottom-0 left-0 right-0 z-40 bg-gray-900/95 backdrop-blur-lg border-t border-gray-700/50 px-4 py-3">
        <div className="flex items-center justify-between">
          {/* Song Info */}
          <div className="flex items-center space-x-4 overflow-hidden">
            <div className="w-10 h-10 bg-gradient-to-br from-purple-500 to-pink-500 rounded-md overflow-hidden flex items-center justify-center">
              {!loading && !error && imageUrl ? (
                <img
                  src={imageUrl}
                  alt={currentMusic.musicName}
                  className="w-full h-full object-cover"
                />
              ) : (
                <Music className="w-5 h-5 text-white" />
              )}
            </div>
            <div className="min-w-0">
              <p className="text-white text-sm font-medium truncate">
                {currentMusic.musicName}
              </p>
              <p className="text-xs text-gray-400 truncate">
                {currentMusic.artistName}
              </p>
            </div>
          </div>

          {/* Controls & Volume */}
          <div className="flex items-center space-x-4 ml-auto">
            {/* Skip / Play Controls */}
            <button
              onClick={onSkipPrevious}
              className="text-gray-400 hover:text-white p-2"
            >
              <SkipBack className="w-4 h-4" />
            </button>

            <button
              onClick={onPlayPause}
              className="p-2 bg-white text-black rounded-full hover:scale-105 transition-transform"
            >
              {isPlaying ? (
                <Pause className="w-4 h-4" fill="currentColor" />
              ) : (
                <Play className="w-4 h-4 ml-0.5" fill="currentColor" />
              )}
            </button>

            <button
              onClick={onSkipNext}
              className="text-gray-400 hover:text-white p-2"
            >
              <SkipForward className="w-4 h-4" />
            </button>

            {/* Volume Controls */}
            <div className="flex items-center space-x-2 ml-4">
              <button
                onClick={() => setVolume((v) => Math.max(0, v - 0.1))}
                className="text-gray-400 hover:text-white"
              >
                <Volume1 className="w-4 h-4" />
              </button>

              <input
                type="range"
                min={0}
                max={1}
                step={0.01}
                value={volume}
                onChange={(e) => setVolume(parseFloat(e.target.value))}
                className="w-24 h-1 rounded-full cursor-pointer volume-slider"
                style={
                  { "--volume-fill": `${volume * 100}%` } as React.CSSProperties
                }
              />

              <button
                onClick={() => setVolume((v) => Math.min(1, v + 0.1))}
                className="text-gray-400 hover:text-white"
              >
                <Volume2 className="w-4 h-4" />
              </button>
            </div>
          </div>
        </div>
        <audio ref={audioRef} />
      </div>
    </>
  );
};
