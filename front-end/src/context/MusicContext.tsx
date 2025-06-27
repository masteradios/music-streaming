import React, { createContext, useContext, ReactNode } from "react";
import { Music } from "../types";
import { useMusicInternal } from "../hooks/useMusicInternal";

type MusicContextType = ReturnType<typeof useMusicInternal>;

const MusicContext = createContext<MusicContextType | null>(null);

interface MusicProviderProps {
  songs: Music[];
  children: ReactNode;
}

export const MusicProvider: React.FC<MusicProviderProps> = ({
  songs,
  children,
}) => {
  const music = useMusicInternal(songs);
  return (
    <MusicContext.Provider value={music}>{children}</MusicContext.Provider>
  );
};

export const useMusic = () => {
  const context = useContext(MusicContext);
  if (!context) {
    throw new Error("useMusic must be used within a MusicProvider");
  }
  return context;
};
