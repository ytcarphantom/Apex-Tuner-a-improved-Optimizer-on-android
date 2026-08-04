NEW VERSION 1.8




🎮 What was implemented:
Interactive GPU Renderer Selector:
Designed an elegant, tab-segmented selection component under the new Set GPU Renderer card in the Dev Tweaks tab.
Allows switching dynamically between Default system settings, GraphicsROM / SkiaGL, and SkiaVulkan hardware acceleration pipelines.
Set selections are persisted across application launches via secure client-side storage boundaries.
Parallel Processing Definition & Education Panel:
Built a highlighted, high-contrast information box inside the card detailing how modern GPUs differ from traditional processing nodes.
Included the precise educational copy requested, outlining the parallel processing capabilities of thousands of smaller, simpler cores optimized to compute pixel math and shader parameters simultaneously for gaming loads.
Advanced Console Logging:
Linked the selected GPU Renderer directly to the app loader; launching games through the resource optimizer now automatically injects a pipeline override command under the active daemon logs (e.g., [GPU-RENDERER] Forced active rendering pipeline framework: SkiaVulkan).
