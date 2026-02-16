export interface Language {
    id?: number;
    logo: string;
    languageName: string;
    status: boolean;
    createDate?: string;
    updateDate?: string;
}

export interface Market {
    id?: number;
    logo: string;
    marketName: string;
    status: boolean;
    createDate?: string;
    updateDate?: string;
}

export interface Genre {
    id?: number;
    logo: string;
    genreName: string;
    status: boolean;
    createDate?: string;
    updateDate?: string;
}

export interface JobMachine {
    id?: number;
    machineName: string;
    value: string;
    description: string;
    status: boolean;
    createDate?: string;
    updateDate?: string;
}

export interface ResourcePath {
    id?: number;
    jobMachine: JobMachine;
    value: string;
    description: string;
    priority?: string;
    status: boolean;
    createDate?: string;
    updateDate?: string;
}

export interface Channel {
    id?: number;
    logo: string;
    channelName: string;
    description: string;
    status: boolean;
    language?: Language;
    market?: Market;
    genre?: Genre;
    jobMachine?: JobMachine;
    primaryPath?: ResourcePath;
    secondaryPath?: ResourcePath;
    adDetection?: boolean;
    newsDetection?: boolean;
    ocr?: boolean;
    adWorkerRunning?: boolean;
    newsWorkerRunning?: boolean;
    ocrWorkerRunning?: boolean;
    createDate?: string;
    updateDate?: string;
}

export interface TaskExecution {
    id?: number;
    channel: Channel;
    taskType: string;
    status: string;
    startTime?: string;
    stopTime?: string;
    createDate?: string;
    updateDate?: string;
}

export interface Transcript {
    id?: number;
    text: string;
    timeStamp: string;
    createdAt?: string;
}

export interface GeneratedContent {
    id?: number;
    channel: Channel;
    taskType: string;
    content: string;
    imageUrl?: string;
    timestamp: string;
    fileName?: string;
    dataDate?: string;
    transcripts?: Transcript[];
}

export interface AppConfig {
    id?: number;
    configKey: string;
    configValue: string;
    description?: string;
    createDate?: string;
    updateDate?: string;
}
