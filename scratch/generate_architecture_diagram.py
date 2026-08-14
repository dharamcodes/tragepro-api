import matplotlib.pyplot as plt
import matplotlib.patches as patches
import os

def create_architecture_diagram():
    fig = plt.figure(figsize=(18, 14), dpi=300)
    ax = fig.add_subplot(111)
    ax.set_xlim(0, 1800)
    ax.set_ylim(0, 1400)
    ax.axis('off')
    
    # Background fill
    fig.patch.set_facecolor('#0F172A') # Dark slate background
    ax.set_facecolor('#0F172A')

    # Color Palette
    PRIMARY_HEADER = '#38BDF8'
    TEXT_COLOR = '#F8FAFC'
    TEXT_MUTED = '#94A3B8'
    
    # Title Header
    ax.text(900, 1340, "TRAGEPRO-API — Spring Modulith Architecture & System Flows", 
            fontsize=22, fontweight='bold', color='#F1F5F9', ha='center', va='center')
    ax.text(900, 1305, "Domain-Driven Flat Architecture with Temporal Workflow Engine, Strategy Pipeline & Concurrent Contexts", 
            fontsize=12, color='#94A3B8', ha='center', va='center')

    # Helper function to draw rounded boxes
    def draw_box(x, y, w, h, bg_color, border_color, title, subtitle=None, title_color='#FFFFFF'):
        rect = patches.FancyBboxPatch((x, y), w, h, boxstyle="round,pad=10,rounding_size=12",
                                      facecolor=bg_color, edgecolor=border_color, linewidth=2, zorder=2)
        ax.add_patch(rect)
        if title:
            ax.text(x + w/2, y + h - 25, title, fontsize=12, fontweight='bold', color=title_color, ha='center', va='center', zorder=3)
        if subtitle:
            ax.text(x + w/2, y + h - 48, subtitle, fontsize=9, color=TEXT_MUTED, ha='center', va='center', zorder=3)

    # 1. CORE MODULE BOX (Root Core Abstractions)
    draw_box(40, 920, 1720, 330, '#1E293B', '#0284C7', "CORE MODULE (com.tragepro.api.core) — Spring Modulith @ApplicationModule(type = OPEN)", 
             "Central abstraction layer providing base contracts for Workflows, Strategy Pipelines, and Auto-Registration")

    # Core sub-boxes
    draw_box(70, 950, 520, 260, '#0F172A', '#38BDF8', "Pipeline Architecture", "Chain of Responsibility Pattern", '#38BDF8')
    ax.text(330, 1130, "• Pipeline<C extends PipelineContext>\n• PipelineStep<C>\n• PipelineContext", 
            fontsize=10, color='#E2E8F0', ha='center', va='top', family='monospace')

    draw_box(630, 950, 540, 260, '#0F172A', '#38BDF8', "Workflow Engine Core", "Template Method & Registry Patterns", '#38BDF8')
    ax.text(900, 1130, "• BaseWorkflow (Marker Interface)\n• BaseActivity (Abstract Class)\n   - globalActivity(Class) / localActivity(Class)\n   - globalActivities() / localActivities()\n• ActivityRegistry (Auto-registers Temporal Beans)\n• WorkflowRegistry (Dynamic Workflow Resolver)", 
            fontsize=9.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    draw_box(1200, 950, 530, 260, '#0F172A', '#38BDF8', "Common Primitives", "Shared Framework Base Utilities", '#38BDF8')
    ax.text(1465, 1130, "• BaseEntity / Base32IdGen\n• BaseMapper / MapperFactory\n• AppException / ErrorType\n• ObjectCloneUtil / TimeframesUtil", 
            fontsize=9.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    # 2. INFRASTRUCTURE MODULE BOX
    draw_box(40, 600, 1720, 280, '#1E293B', '#475569', "INFRA MODULE (com.tragepro.api.infra) — Cross-Cutting Infrastructure")

    draw_box(70, 630, 390, 220, '#0F172A', '#818CF8', "Security Layer", "Spring Security & JWT", '#818CF8')
    ax.text(265, 780, "• SecurityConfig\n• AuthConfig\n• JWTAuthFilter\n• JwtTokenHelper", fontsize=9.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    draw_box(490, 630, 390, 220, '#0F172A', '#F43F5E', "Temporal Worker Config", "Decoupled Registration", '#F43F5E')
    ax.text(685, 780, "• TemporalConfig\n  - ActivityRegistry Integration\n  - WorkflowRegistry Discovery\n  - WorkerPool Auto-Wiring", fontsize=9.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    draw_box(910, 630, 410, 220, '#0F172A', '#34D399', "Persistence Config", "MongoDB Auditing", '#34D399')
    ax.text(1115, 780, "• MongoAuditConfig\n• Spring Data Mongo\n• Custom Repositories", fontsize=9.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    draw_box(1350, 630, 380, 220, '#0F172A', '#FBBF24', "Web & OpenAPI", "Swagger & Documentation", '#FBBF24')
    ax.text(1540, 780, "• OpenApiConfig\n• SwaggerFilter\n• HTTP Exchange Clients", fontsize=9.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    # 3. DOMAIN MODULES BOX
    draw_box(40, 170, 1720, 400, '#1E293B', '#10B981', "DOMAIN MODULES — Domain-Driven Business Logic")

    # Domain module 1: marketdata
    draw_box(70, 200, 520, 340, '#0F172A', '#06B6D4', "marketdata Module", "Feed Adapters & Concurrent Contexts", '#06B6D4')
    ax.text(330, 490, "• feed/\n  - DataFeedAdapter (Public Interface)\n  - FeedAdapterFactory (Factory Pattern)\n  - DummyFeedAdapter / FeedClientAdapter\n• context/\n  - DatafeedContext (ConcurrentHashMap)\n  - WatchlistContext (Thread-Safe KeySet)\n• service/ & web/", 
            fontsize=9, color='#E2E8F0', ha='center', va='top', family='monospace')

    # Domain module 2: strategy
    draw_box(620, 200, 560, 340, '#0F172A', '#A855F7', "strategy Module", "Pipeline & Temporal Activity Impl", '#A855F7')
    ax.text(900, 490, "• pipeline/\n  - builder/ (OHLCV, Volume, VWAP, Levels)\n  - evaluator/ (Liquidity, Signals)\n  - executor/ (Buy, Sell, Notify)\n• workflow/\n  - DataInitWorkflow / WorkflowProvider\n  - BuilderActivityImpl / DataInitActivityImpl\n    (Extends core.BaseActivity)\n• definition/ (IntradayStrategy, SwingStrategy)", 
            fontsize=8.5, color='#E2E8F0', ha='center', va='top', family='monospace')

    # Domain module 3: Bounded Context Domains (identity, journal, trading, notification)
    draw_box(1210, 200, 520, 340, '#0F172A', '#F59E0B', "Bounded Context Modules", "Identity, Journal, Trading, Notification", '#F59E0B')
    ax.text(1470, 490, "• identity/ (Auth & User Management)\n• journal/ (Trade Journaling & Notes)\n• trading/ (Order Execution & Accounts)\n• notification/ (Alert Events & Channels)\n  - AlertEventPublisher\n  - AlertEventListener", 
            fontsize=9, color='#E2E8F0', ha='center', va='top', family='monospace')

    # 4. SYSTEM FLOWS BANNER
    draw_box(40, 30, 1720, 110, '#0284C7', '#38BDF8', "6 CORE SYSTEM FLOWS IN TRAGEPRO-API", None, '#FFFFFF')
    flow_text = (
        "Flow 1: Client -> JWTAuthFilter -> UserDetailsService -> Security Context  |  "
        "Flow 2: Client -> DataFeedController -> DatafeedServiceImpl -> FeedAdapterFactory -> DatafeedContext  |  "
        "Flow 3: Temporal Worker -> TemporalConfig -> WorkflowRegistry -> DataInitWorkflowImpl -> DataInitActivityImpl  |  \n"
        "Flow 4: Strategy Request -> StrategyBuilder (Chain) -> StrategyEvaluator -> StrategyExecutor -> StrategyResponse  |  "
        "Flow 5: Client -> JournalController -> JournalServiceImpl -> Mongo  |  "
        "Flow 6: Event -> AlertEventPublisher -> Spring Event -> AlertEventListener -> Notification Channel"
    )
    ax.text(900, 68, flow_text, fontsize=8.5, color='#FFFFFF', ha='center', va='center', fontweight='bold')

    plt.tight_layout()
    os.makedirs("docs", exist_ok=True)
    plt.savefig("docs/architecture.png", format='png', bbox_inches='tight', facecolor=fig.get_facecolor(), edgecolor='none')
    plt.close()
    print("Successfully generated docs/architecture.png")

if __name__ == "__main__":
    create_architecture_diagram()
