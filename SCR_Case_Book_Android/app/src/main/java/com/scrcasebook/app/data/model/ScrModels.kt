package com.scrcasebook.app.data.model

data class ScrSection(
    val index: Int,
    val key: String,
    val title: String,
    val pageRange: String,
    val requiredPages: Int,
    val hint: String,
    val hasChiefComplaintColumns: Boolean = false
)

enum class PalmRejectionMode(val label: String) {
    PEN_ONLY("✏️ Stylus draw · touch scroll"),
    PEN_AND_FINGER("✏️ + ☝️ Stylus & touch draw"),
    SCROLL("✋ Scroll only")
}

data class Stroke(
    val c: String = "#111111",      // Color hex
    val w: Float = 2.5f,             // Width in dp / pixels
    val e: Int = 0,                  // 1 if eraser, 0 if pen
    val p: List<Float> = emptyList() // Flattened coordinates [x0, y0, x1, y1, ...]
)

object ScrDefinitions {
    const val STANDARD_RECORD_TOTAL_PAGES = 106

    val PALETTE = listOf("#111111", "#1747C9", "#C01818")

    val SECTIONS = listOf(
        ScrSection(
            index = 0,
            key = "s0",
            title = "Case particulars",
            pageRange = "Page 1",
            requiredPages = 1,
            hint = "S. No · Physician · Observer · OPD/IPD Reg. No. · Date · Diagnosis · Age · Sex · Education · Occupation · Religion · Address · Contact"
        ),
        ScrSection(
            index = 1,
            key = "s1",
            title = "Case summary",
            pageRange = "Page 2",
            requiredPages = 1,
            hint = "Brief structured clinical summary of the case"
        ),
        ScrSection(
            index = 2,
            key = "s2",
            title = "A. Present history — chief complaint(s)",
            pageRange = "Pages 3–5",
            requiredPages = 3,
            hint = "No. · Location (area, direction, spread, tissue, organ, system & duration) · Sensation & pathology · Modalities (A.F., <, >) · Accompaniments (strict time relation)",
            hasChiefComplaintColumns = true
        ),
        ScrSection(
            index = 3,
            key = "s3",
            title = "Associated complaints (concomitants)",
            pageRange = "Pages 6–8",
            requiredPages = 3,
            hint = "Concomitant symptoms appearing alongside chief complaints"
        ),
        ScrSection(
            index = 4,
            key = "s4",
            title = "III.A.1 Physical characteristics",
            pageRange = "Pages 9–11",
            requiredPages = 3,
            hint = "Appearance · build · weight · face · hands · gait · deformities · skin · hair · nails · eyes · ears · mouth · teeth · coldness / heat · oedema · perspiration · general impression & type"
        ),
        ScrSection(
            index = 5,
            key = "s5",
            title = "III.A.2 Digestion",
            pageRange = "Page 12",
            requiredPages = 1,
            hint = "Acidity · appetite · colics · eructations · flatulence · hunger · nausea · taste · thirst · vomiting · aversions (A) & cravings (C)"
        ),
        ScrSection(
            index = 6,
            key = "s6",
            title = "III.A.3 Eliminations",
            pageRange = "Page 13",
            requiredPages = 1,
            hint = "Stool · urine · abnormal discharges"
        ),
        ScrSection(
            index = 7,
            key = "s7",
            title = "III.A.4 Menstrual function",
            pageRange = "Pages 14–15",
            requiredPages = 2,
            hint = "FMP · LMP · menarche · menses · flow · leucorrhoea · concomitants"
        ),
        ScrSection(
            index = 8,
            key = "s8",
            title = "III.A.5 Sexual function",
            pageRange = "Page 16",
            requiredPages = 1,
            hint = "Desire · masturbation · coitus · erection · ejaculation · orgasm · vagina"
        ),
        ScrSection(
            index = 9,
            key = "s9",
            title = "III.A.6 Patient’s / mother’s obstetric history",
            pageRange = "Pages 17–19",
            requiredPages = 3,
            hint = "Pregnancies · antenatal · labour · delivery · post-partum · particulars of each pregnancy"
        ),
        ScrSection(
            index = 10,
            key = "s10",
            title = "III.A.7 Developmental landmarks & problems",
            pageRange = "Pages 20–21",
            requiredPages = 2,
            hint = "Milestones · speech · control · feeding · behaviour · parental attitudes"
        ),
        ScrSection(
            index = 11,
            key = "s11",
            title = "III.A.8 Diet & daily routine",
            pageRange = "Page 22",
            requiredPages = 1,
            hint = "Meal timings, hydration, sleep-wake cycles, physical routine"
        ),
        ScrSection(
            index = 12,
            key = "s12",
            title = "III.B Mental state & I.P.R.",
            pageRange = "Pages 23–24, 31–40, 44–46",
            requiredPages = 15,
            hint = "Mental state, inter-personal relations, detailed mental totality and interpretation"
        ),
        ScrSection(
            index = 13,
            key = "s13",
            title = "Life-space investigation",
            pageRange = "Pages 25–30",
            requiredPages = 6,
            hint = "Society · family · inter-personal · dissatisfaction"
        ),
        ScrSection(
            index = 14,
            key = "s14",
            title = "III.C Reactions – physical factors",
            pageRange = "Pages 47–54",
            requiredPages = 8,
            hint = "Time · temperature · motion & position · meteorological · sensory inputs · physiological functions"
        ),
        ScrSection(
            index = 15,
            key = "s15",
            title = "Dreams: analysis",
            pageRange = "Pages 41–43",
            requiredPages = 3,
            hint = "Recurring themes, symbolism, emotional residue upon waking"
        ),
        ScrSection(
            index = 16,
            key = "s16",
            title = "III.D Fever totality",
            pageRange = "Pages 55–56",
            requiredPages = 2,
            hint = "Onset · type · course · chill · heat · perspiration · compound fevers · apyrexia"
        ),
        ScrSection(
            index = 17,
            key = "s17",
            title = "III.E Original unmodified picture",
            pageRange = "Page 57",
            requiredPages = 1,
            hint = "Natural course of illness before suppressive medications or therapies"
        ),
        ScrSection(
            index = 18,
            key = "s18",
            title = "III.F Chronological sequence",
            pageRange = "Page 58",
            requiredPages = 1,
            hint = "Timeline of ailments from birth to present presentation"
        ),
        ScrSection(
            index = 19,
            key = "s19",
            title = "Past history + physical/systemic examination",
            pageRange = "Pages 59–66",
            requiredPages = 8,
            hint = "P/H · F/H · pathology checklist · physical examination · systemic examination"
        ),
        ScrSection(
            index = 20,
            key = "s20",
            title = "Provisional clinical & differential diagnosis",
            pageRange = "Page 67",
            requiredPages = 1,
            hint = "Clinical investigations, differential diagnosis, diagnostic rationale"
        ),
        ScrSection(
            index = 21,
            key = "s21",
            title = "Investigations",
            pageRange = "Pages 68–73",
            requiredPages = 6,
            hint = "Investigation · report · dates"
        ),
        ScrSection(
            index = 22,
            key = "s22",
            title = "Evolutionary Hahnemannian totality & miasmatic expression",
            pageRange = "Pages 74–85",
            requiredPages = 12,
            hint = "Evolutionary totality model, miasmatic expression and progression mapping"
        ),
        ScrSection(
            index = 23,
            key = "s23",
            title = "Repertorial totality & differential field",
            pageRange = "Pages 86–89",
            requiredPages = 4,
            hint = "Rubrics selected, repertorization chart, grading & analysis"
        ),
        ScrSection(
            index = 24,
            key = "s24",
            title = "Remedy correspondences & final selection",
            pageRange = "Pages 90–93",
            requiredPages = 4,
            hint = "Constitutional · acute · intercurrent totalities (state grounds of final selection)"
        ),
        ScrSection(
            index = 25,
            key = "s25",
            title = "Rx planning, prescription, expected changes & progress notes",
            pageRange = "Pages 94–106",
            requiredPages = 13,
            hint = "Rx planning/programming, prescription & management, therapeutic problem resolution, expected changes, progress records"
        )
    )
}
