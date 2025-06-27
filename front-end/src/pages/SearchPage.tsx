import React, { useState, useEffect } from "react";
import { Search, Loader2 } from "lucide-react";
import { Navbar } from "../components/Navbar";
import { MusicCard } from "../components/MusicCard";
import { MusicPlayer } from "../components/MusicPlayer";
import { musicAPI } from "../services/api";
import { useMusicInternal } from "../hooks/useMusicInternal";
import { Music } from "../types";

export const SearchPage: React.FC = () => {
  const [keyword, setKeyword] = useState("");
  const [results, setResults] = useState<Music[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [hasSearched, setHasSearched] = useState(false);
  const [searchType, setSearchType] = useState<"all" | "artist" | "album">(
    "all"
  );


  const {
    currentMusicId,
    playMusic,
    skipToNext,
    skipToPrevious,
    togglePlayPause,
    isPlaying,
    blobUrl,
  } = useMusicInternal(results);

const handleSearch = async (searchTerm: string) => {
  if (!searchTerm.trim()) {
    setResults([]);
    setHasSearched(false);
    return;
  }

  setLoading(true);
  setError("");
  setHasSearched(true);

  try {
    let data: Music[] = [];

    switch (searchType) {
      case "artist":
        data = await musicAPI.searchByArtist(searchTerm);
        break;
      case "album":
        data = await musicAPI.searchByAlbum(searchTerm);
        break;
      default:
        data = await musicAPI.search(searchTerm);
    }

    setResults(data);
  } catch (err) {
    setError("Search failed. Please try again.");
    setResults([]);
  } finally {
    setLoading(false);
  }
};


  useEffect(() => {
    const timeoutId = setTimeout(() => {
      handleSearch(keyword);
    }, 500);

    return () => clearTimeout(timeoutId);
  }, [keyword]);

  const handlePlayMusic = async (musicId: string) => {
    try {
      await playMusic(musicId);
    } catch (err) {
      console.error("Failed to play music:", err);
    }
  };

  const currentMusic = currentMusicId
    ? results.find((song) => song.id === currentMusicId)
    : null;

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900">
      <Navbar />

      <main className="max-w-6xl mx-auto px-6 py-8 pb-24">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-white mb-6">Search Music</h1>
          <div className="mb-4 flex gap-4">
            {["all", "artist", "album"].map((type) => (
              <button
                key={type}
                onClick={() => {
                  setSearchType(type as "all" | "artist" | "album");
                  handleSearch(keyword); // re-trigger search on change
                }}
                className={`px-4 py-2 rounded-md text-sm font-medium transition-colors ${
                  searchType === type
                    ? "bg-purple-600 text-white"
                    : "bg-gray-700 text-gray-300 hover:bg-gray-600"
                }`}
              >
                {type.charAt(0).toUpperCase() + type.slice(1)}
              </button>
            ))}
          </div>

          <div className="relative w-full">
            <Search className="absolute left-4 top-1/2 transform -translate-y-1/2 w-full h-5 text-gray-400" />
            <input
              type="text"
              value={keyword}
              onChange={(e) => setKeyword(e.target.value)}
              placeholder="Search for songs, artists, or albums..."
              className="w-full pl-12 pr-4 py-4 bg-gray-800/50 backdrop-blur-sm border border-gray-600 rounded-xl text-white placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:border-transparent text-lg"
            />
          </div>
        </div>

        {loading && (
          <div className="flex items-center justify-center py-16">
            <div className="text-center">
              <Loader2 className="w-8 h-8 animate-spin text-purple-500 mx-auto mb-4" />
              <p className="text-gray-400">Searching...</p>
            </div>
          </div>
        )}

        {error && (
          <div className="text-center py-16">
            <p className="text-red-400 mb-4">{error}</p>
            <button
              onClick={() => handleSearch(keyword)}
              className="px-6 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
            >
              Try Again
            </button>
          </div>
        )}

        {!loading && !error && hasSearched && (
          <>
            {results.length === 0 ? (
              <div className="text-center py-16">
                <p className="text-gray-400 text-lg">
                  No results found for "{keyword}". Try a different search term.
                </p>
              </div>
            ) : (
              <>
                <div className="mb-6">
                  <p className="text-gray-400">
                    Found {results.length} result
                    {results.length !== 1 ? "s" : ""} for "{keyword}"
                  </p>
                </div>

                <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
                  {results.map((song) => (
                    <MusicCard
                      key={song.id}
                      music={song}
                      onPlay={handlePlayMusic}
                      isPlaying={currentMusicId === song.id && isPlaying}
                    />
                  ))}
                </div>
              </>
            )}
          </>
        )}

        {!hasSearched && (
          <div className="text-center py-16">
            <Search className="w-16 h-16 text-gray-600 mx-auto mb-4" />
            <p className="text-gray-400 text-lg">
              Start typing to search for music
            </p>
          </div>
        )}
      </main>

      {currentMusic && (
        <MusicPlayer
          currentMusic={currentMusic}
          isPlaying={isPlaying}
          onPlayPause={togglePlayPause}
          onSkipNext={skipToNext}
          onSkipPrevious={skipToPrevious}
          blobUrl={blobUrl}
        />
      )}
    </div>
  );
};
