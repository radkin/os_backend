CREATE TABLE IF NOT EXISTS user_preferences
(
    id                  SERIAL PRIMARY KEY,
    user_id             INTEGER,
    my_state_only       BOOLEAN,
    my_party_only       BOOLEAN,
    my_county_only      BOOLEAN,
    twitter_hide        BOOLEAN,
    facebook_hide       BOOLEAN,
    youtube_hide        BOOLEAN,
    google_entity_hide  BOOLEAN,
    cspan_hide          BOOLEAN,
    vote_smart_hide     BOOLEAN,
    gov_track_hide      BOOLEAN,
    open_secrets_hide   BOOLEAN,
    vote_view_hide      BOOLEAN,
    fec_hide            BOOLEAN
)