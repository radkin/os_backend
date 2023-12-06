    create table senators (
        id                      serial
            primary key,
        pp_id                   text,
        title                   text,
        short_title             text,
        api_uri                 text,
        first_name              text,
        middle_name             text,
        last_name               text,
        suffix                  text,
        date_of_birth           date,
        gender                  text,
        party                   text,
        leadership_role         text,
        twitter_account         text,
        facebook_account        text,
        youtube_account         text,
        govtrack_id             bigint,
        cspan_id                bigint,
        votesmart_id            bigint,
        icpsr_id                bigint,
        crp_id                  text,
        google_entity_id        text,
        fec_candidate_id        text,
        url                     text,
        rss_url                 text,
        contact_form            text,
        in_office               boolean,
        dw_nominate             double precision,
        seniority               integer,
        next_election           text,
        total_votes             integer,
        missed_votes            integer,
        total_present           integer,
        last_updated            date,
        ocd_id                  text,
        office                  text,
        phone                   text,
        state                   text,
        senate_class            integer,
        state_rank              text,
        lis_id                  text,
        missed_votes_pct        double precision,
        votes_with_party_pct    double precision,
        votes_against_party_pct double precision
    );
  
    create table users (
        id             serial
            primary key,
        inajar_api_key text,
        email          text,
        first_name     text,
        last_name      text,
        profile_img    text,
        password       varchar(255),
        state          varchar(2),
        party          varchar(50),
        is_enabled     boolean,
        gender         varchar(15),
        is_logged_in   boolean
    );
    
    create table congress (
        id                      serial
            primary key,
        pp_id                   text,
        title                   text,
        short_title             text,
        api_uri                 text,
        first_name              text,
        middle_name             text,
        last_name               text,
        suffix                  text,
        date_of_birth           date,
        gender                  text,
        party                   text,
        leadership_role         text,
        twitter_account         text,
        facebook_account        text,
        youtube_account         text,
        govtrack_id             bigint,
        cspan_id                bigint,
        votesmart_id            bigint,
        icpsr_id                bigint,
        crp_id                  text,
        google_entity_id        text,
        fec_candidate_id        text,
        url                     text,
        rss_url                 text,
        contact_form            text,
        in_office               boolean,
        dw_nominate             double precision,
        seniority               integer,
        next_election           text,
        total_votes             integer,
        missed_votes            integer,
        total_present           integer,
        last_updated            date,
        ocd_id                  text,
        office                  text,
        phone                   text,
        district                text,
        at_large                boolean,
        geoid                   text,
        missed_votes_pct        double precision,
        votes_with_party_pct    double precision,
        votes_against_party_pct double precision,
        state                   varchar(255)
    );
    
    create table user_preferences (
        id                 serial
            primary key,
        user_id            integer
            constraint fkepakpib0qnm82vmaiismkqf88
            references users,
        my_state_only      boolean,
        my_party_only      boolean,
        my_county_only     boolean,
        twitter_hide       boolean,
        facebook_hide      boolean,
        youtube_hide       boolean,
        google_entity_hide boolean,
        cspan_hide         boolean,
        vote_smart_hide    boolean,
        gov_track_hide     boolean,
        open_secrets_hide  boolean,
        vote_view_hide     boolean,
        fec_hide           boolean
    );
    
    create table sectors (
        id          serial
            primary key,
        cid         text,
        cycle       integer,
        sector_name text,
        sectorid    text,
        indivs      integer,
        pacs        integer,
        total       integer
    );
    
    create table contributors (
        id            serial
            primary key,
        cid           text,
        cycle         integer,
        org_name      text,
        contributorid text,
        total         integer,
        pacs          integer,
        indivs        integer
    );
    
    create table sponsors (
        id                          serial
            primary key,
        contribution_receipt_amount numeric(18, 2),
        contribution_receipt_date   text,
        contributor_aggregate_ytd   numeric(18, 2),
        contributor_city            text,
        contributor_employer        text,
        contributor_first_name      text,
        contributor_last_name       text,
        contributor_middle_name     text,
        contributor_name            text,
        contributor_occupation      text,
        contributor_state           text,
        contributor_street_1        text,
        contributor_street_2        text,
        contributor_zip             text,
        congress_id                 integer
            constraint fkj3pagb2dm24hgsfh5y7t0esie
                references congress,
        senator_id                  integer
            constraint fkgh86900671erfo1oykajenrea
                references senators
    );
    
    create table donations (
        id               serial
            primary key,
        amount           numeric(18, 2),
        date_of_donation date,
        sponsor_id       integer
            constraint fkgyxsc7v12adnuqvqhp9wj3ls2
                references sponsors,
        pp_id            text
    );
    
    create table committees (
        id                          serial
            primary key,
        fec_committee_id            text,
        two_year_transaction_period integer,
        pp_id                       text
    );
    
    create table sponsor_senators (
        id         serial
            primary key,
        sponsor_id integer
            constraint fkob0bugwm6usiry0jpfcdi4vcg
                references sponsors,
        senator_id integer
            constraint fk3q3kg3dq2pwqy10fe8xwfjii9
                references senators
    );
    
    create table sponsor_congress (
        id          serial
            primary key,
        sponsor_id  integer
            constraint fk6r699f6xjhdho5enft9xlos98
                references sponsors,
        congress_id integer
            constraint fkaem800pidevxaocpgdgbryeqc
                references congress
    );
        
    insert into users(inajar_api_key,email,first_name,last_name) values('8c174428e7d25fb2b6702ed6b965d16d', 'admin@gmail.com', 'admin', 'admin');
    
    CREATE TABLE log_data
    (
        id        UUID PRIMARY KEY,
        item_name VARCHAR(255) NOT NULL
    );
