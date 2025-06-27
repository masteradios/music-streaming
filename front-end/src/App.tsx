import React, { useEffect, useState } from "react";
import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
} from "react-router-dom";
import { AuthProvider } from "./context/AuthContext";
import { ProtectedRoute } from "./components/ProtectedRoute";
import { SignupPage } from "./pages/SignupPage";
import { LoginPage } from "./pages/LoginPage";
import { HomePage } from "./pages/HomePage";
import { SearchPage } from "./pages/SearchPage";
import { musicAPI } from "./services/api";
import { Music } from "./types";
import { MusicPlayer } from "./components/MusicPlayer";
import { MusicProvider, useMusic } from "./context/MusicContext";

// ✅ Now we accept songs from the parent
const AppContent: React.FC<{ songs: Music[] }> = ({ songs }) => {
  const {
    currentMusicId,
    isPlaying,
    blobUrl,
    togglePlayPause,
    skipToNext,
    skipToPrevious,
  } = useMusic();

  const currentMusic = currentMusicId
    ? songs.find((song) => song.id === currentMusicId)
    : null;

  return (
    <>
      <Routes>
        <Route path="/" element={<Navigate to="/login" replace />} />
        <Route path="/signup" element={<SignupPage />} />
        <Route path="/login" element={<LoginPage />} />
        <Route
          path="/home"
          element={
            <ProtectedRoute>
              <HomePage />
            </ProtectedRoute>
          }
        />
        <Route
          path="/search"
          element={
            <ProtectedRoute>
              <SearchPage />
            </ProtectedRoute>
          }
        />
      </Routes>

      {/* ✅ Use currentMusic only if found */}
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
    </>
  );
};

function App() {
  const [songs, setSongs] = useState<Music[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchSongs = async () => {
      try {
        const data = await musicAPI.getSongs();
        setSongs(data);
      } catch (err) {
        console.error("Failed to load songs:", err);
      } finally {
        setLoading(false);
      }
    };

    fetchSongs();
  }, []);

  if (loading) {
    return (
      <div className="min-h-screen bg-gray-900 flex items-center justify-center text-white">
        Loading songs...
      </div>
    );
  }

  return (
    <AuthProvider>
      <MusicProvider songs={songs}>
        <Router>
          {/* ✅ Pass songs to AppContent */}
          <AppContent songs={songs} />
        </Router>
      </MusicProvider>
    </AuthProvider>
  );
}

export default App;
