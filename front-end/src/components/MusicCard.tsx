import React from "react";
import { Play, Music, Pause } from "lucide-react";
import { Music as MusicType } from "../types";
import { useMusicImage } from "../hooks/useMusicImage";

interface MusicCardProps {
  music: MusicType;
  onPlay: (musicId: string) => void;
  isPlaying?: boolean;
}

export const MusicCard: React.FC<MusicCardProps> = ({
  music,
  onPlay,
  isPlaying,
}) => {
  const { imageUrl, loading, error } = useMusicImage(music.id);

  const handlePlay = () => {
    onPlay(music.id);
  };

  return (
    <div className="group bg-gray-800/50 backdrop-blur-sm rounded-xl p-4 hover:bg-gray-700/50 transition-all duration-300 hover:scale-105 border border-gray-700/50">
      <div className="relative mb-4">
        <div className="aspect-square w-full bg-gradient-to-br from-purple-500 to-pink-500 rounded-lg flex items-center justify-center overflow-hidden">
          {!loading && !error && imageUrl ? (
            <img
              src={imageUrl}
              alt={music.musicName}
              className="w-full h-full object-cover"
            />
          ) : (
            <Music className="w-12 h-12 text-white" />
          )}
        </div>
        <button
          onClick={handlePlay}
          className={`absolute top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 w-12 h-12 rounded-full flex items-center justify-center transition-all duration-300 ${
            isPlaying
              ? "bg-green-500 scale-100 opacity-100"
              : "bg-white/90 hover:bg-white scale-0 group-hover:scale-100 opacity-0 group-hover:opacity-100"
          }`}
        >
          {isPlaying ? (
            <Pause className="w-5 h-5 text-black" fill="currentColor" />
          ) : (
            <Play className="w-5 h-5 text-black ml-0.5" fill="currentColor" />
          )}
        </button>
      </div>

      <div className="space-y-2">
        <h3 className="font-semibold text-white truncate text-sm">
          {music.musicName}
        </h3>
        <p className="text-gray-400 text-xs truncate">{music.artistName}</p>
        <p className="text-gray-500 text-xs">{music.album}</p>
      </div>
    </div>
  );
};
