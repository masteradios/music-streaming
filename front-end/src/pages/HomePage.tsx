// import React, { useState, useEffect } from "react";
// import { Navbar } from "../components/Navbar";
// import { MusicCard } from "../components/MusicCard";
// import { MusicPlayer } from "../components/MusicPlayer";
// import { musicAPI } from "../services/api";
// import { useMusicInternal } from "../hooks/useMusicInternal";
// import { Music } from "../types";
// import { Loader2 } from "lucide-react";

// export const HomePage: React.FC = () => {
//   const [songs, setSongs] = useState<Music[]>([]);
//   const [loading, setLoading] = useState(true);
//   const [error, setError] = useState("");

//   useEffect(() => {
//     fetchSongs();
//   }, []);

//   const fetchSongs = async () => {
//     try {
//       const data = await musicAPI.getSongs();
//       setSongs(data);
//     } catch (err: any) {
//       setError("Failed to load songs. Please try again.");
//     } finally {
//       setLoading(false);
//     }
//   };

//   // Initialize music logic AFTER songs are fetched
//   const {
//     currentMusicId,
//     playMusic,
//     skipToNext,
//     skipToPrevious,
//     togglePlayPause,
//     isPlaying,
//     blobUrl,
//   } = useMusicInternal(songs);

//   const handlePlayMusic = async (musicId: string) => {
//     try {
//       await playMusic(musicId);
//     } catch (err) {
//       console.error("Failed to play music:", err);
//     }
//   };

//   const currentMusic = currentMusicId
//     ? songs.find((song) => song.id === currentMusicId)
//     : null;

//   if (loading) {
//     return (
//       <div className="min-h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900">
//         <Navbar />
//         <div className="flex items-center justify-center h-[calc(100vh-80px)]">
//           <div className="text-center">
//             <Loader2 className="w-8 h-8 animate-spin text-purple-500 mx-auto mb-4" />
//             <p className="text-gray-400">Loading your music...</p>
//           </div>
//         </div>
//       </div>
//     );
//   }

//   if (error) {
//     return (
//       <div className="min-h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900">
//         <Navbar />
//         <div className="flex items-center justify-center h-[calc(100vh-80px)]">
//           <div className="text-center">
//             <p className="text-red-400 mb-4">{error}</p>
//             <button
//               onClick={fetchSongs}
//               className="px-6 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
//             >
//               Try Again
//             </button>
//           </div>
//         </div>
//       </div>
//     );
//   }

//   return (
//     <div className="min-h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900">
//       <Navbar />

//       <main className="max-w-6xl mx-auto px-6 py-8 pb-24">
//         <div className="mb-8">
//           <h1 className="text-3xl font-bold text-white mb-2">
//             Welcome to Melodies of Shreya: Handpicked Classics
//           </h1>
//           <p className="text-gray-400 font-dancing italic">
//             Relive the golden voice through every note...
//           </p>
//         </div>

//         {songs.length === 0 ? (
//           <div className="text-center py-16">
//             <p className="text-gray-400 text-lg">
//               No songs available at the moment.
//             </p>
//           </div>
//         ) : (
//           <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
//             {songs.map((song) => (
//               <MusicCard
//                 key={song.id}
//                 music={song}
//                 onPlay={handlePlayMusic}
//                 isPlaying={currentMusicId === song.id && isPlaying}
//               />
//             ))}
//           </div>
//         )}
//       </main>

//       {currentMusic && (
//         <MusicPlayer
//           currentMusic={currentMusic}
//           isPlaying={isPlaying}
//           onPlayPause={togglePlayPause}
//           onSkipNext={skipToNext}
//           onSkipPrevious={skipToPrevious}
//           blobUrl={blobUrl}
//         />
//       )}
//     </div>
//   );
// };


import React, { useState, useEffect } from "react";
import { Navbar } from "../components/Navbar";
import { MusicCard } from "../components/MusicCard";
import { MusicPlayer } from "../components/MusicPlayer";
import { musicAPI } from "../services/api";
import { useMusicInternal } from "../hooks/useMusicInternal";
import { Music } from "../types";
import { Loader2 } from "lucide-react";

export const HomePage: React.FC = () => {
  const [songs, setSongs] = useState<Music[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    fetchSongs();
  }, []);

  const fetchSongs = async () => {
    try {
      const data = await musicAPI.getSongs();
      setSongs(data);
    } catch (err: any) {
      setError("Failed to load songs. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  const {
    currentMusicId,
    playMusic,
    skipToNext,
    skipToPrevious,
    togglePlayPause,
    isPlaying,
    blobUrl,
  } = useMusicInternal(songs);

  const handlePlayMusic = async (musicId: string) => {
    try {
      await playMusic(musicId);
    } catch (err) {
      console.error("Failed to play music:", err);
    }
  };

  const currentMusic = currentMusicId
    ? songs.find((song) => song.id === currentMusicId)
    : null;

  if (loading) {
    return (
      <div className="h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900">
        <Navbar />
        <div className="flex items-center justify-center h-[calc(100vh-80px)]">
          <div className="text-center">
            <Loader2 className="w-8 h-8 animate-spin text-purple-500 mx-auto mb-4" />
            <p className="text-gray-400">Loading your music...</p>
          </div>
        </div>
      </div>
    );
  }

  if (error) {
    return (
      <div className="h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900">
        <Navbar />
        <div className="flex items-center justify-center h-[calc(100vh-80px)]">
          <div className="text-center">
            <p className="text-red-400 mb-4">{error}</p>
            <button
              onClick={fetchSongs}
              className="px-6 py-2 bg-purple-600 text-white rounded-lg hover:bg-purple-700 transition-colors"
            >
              Try Again
            </button>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="h-screen bg-gradient-to-br from-gray-900 via-purple-900 to-gray-900 flex flex-col">
      <Navbar />

      {/* Scrollable Main Content (excluding navbar and player) */}
      <main className="flex-1 overflow-y-auto px-6 py-8 pb-36">
        <div className="mb-8">
          <h1 className="text-3xl font-bold text-white mb-2">
            Welcome to Melodies of Shreya: Handpicked Classics
          </h1>
          <p className="text-gray-400 font-dancing italic">
            Relive the golden voice through every note...
          </p>
        </div>

        {songs.length === 0 ? (
          <div className="text-center py-16">
            <p className="text-gray-400 text-lg">
              No songs available at the moment.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 xl:grid-cols-5 gap-6">
            {songs.map((song) => (
              <MusicCard
                key={song.id}
                music={song}
                onPlay={handlePlayMusic}
                isPlaying={currentMusicId === song.id && isPlaying}
              />
            ))}
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
