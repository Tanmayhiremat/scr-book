package com.scrcasebook.app.data.model

data class ScrSection(
    val index: Int,
    val key: String,
    val title: String,
    val pageRange: String = "",
    val requiredPages: Int = 1,
    val hint: String,
    val pages: List<Int> = emptyList(),
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
            hint = "S. No · Physician · Observer · OPD / IPD Reg. No. · Date · Diagnosis · Age · Sex · Education · Occupation · Status · Religion · Veg / N.V. · Spouse · Father · Mother · Siblings · Children · Address · Tel",
            pages = listOf(1)
        ),
        ScrSection(
            index = 1,
            key = "s1",
            title = "Case summary",
            pageRange = "Page 2",
            requiredPages = 1,
            hint = "Comprehensive narrative summary of presenting illness, constitution, and chief clinical findings",
            pages = listOf(2)
        ),
        ScrSection(
            index = 2,
            key = "s2",
            title = "A.I Chief complaint(s)",
            pageRange = "Pages 3–5",
            requiredPages = 3,
            hint = "No. · Location (area, direction, spread, tissue, organ, system & duration) · Sensation & pathology · Modalities (A.F., <, >) · Accompaniments (strict time relation)",
            pages = listOf(3, 4, 5),
            hasChiefComplaintColumns = true
        ),
        ScrSection(
            index = 3,
            key = "s3",
            title = "A.II Associated complaints (concomitants)",
            pageRange = "Pages 6–8",
            requiredPages = 3,
            hint = "No. · Complaint description · Duration & course · Modalities (< / >) · Relationship to chief complaint",
            pages = listOf(6, 7, 8)
        ),
        ScrSection(
            index = 4,
            key = "s4",
            title = "III.A.1 Physical characteristics",
            pageRange = "Pages 9–11",
            requiredPages = 3,
            hint = "Appearance · Build · Stature · Facial configuration · Skin · Hair · Nails · Head · Sense organs · Thermals · Perspiration · General impression",
            pages = listOf(9, 10, 11)
        ),
        ScrSection(
            index = 5,
            key = "s5",
            title = "III.A.2 Digestion",
            pageRange = "Page 12",
            requiredPages = 1,
            hint = "Acidity · Appetite · Colics · Eructations · Flatulence · Hunger · Nausea · Taste · Thirst · Vomiting · Aversions (A) & Cravings (C)",
            pages = listOf(12)
        ),
        ScrSection(
            index = 6,
            key = "s6",
            title = "III.A.3 Eliminations & Menstrual function",
            pageRange = "Pages 13–15",
            requiredPages = 3,
            hint = "Stool · Urine · Abnormal discharges · FMP · LMP · Menses character · Concomitants · Obstetric aftermath · Leucorrhoea",
            pages = listOf(13, 14, 15)
        ),
        ScrSection(
            index = 7,
            key = "s7",
            title = "III.A.5 Sexual function",
            pageRange = "Page 16",
            requiredPages = 1,
            hint = "Desire · Masturbation · Coition · Erection · Ejaculation · Orgasm · Prepuce · Vagina",
            pages = listOf(16)
        ),
        ScrSection(
            index = 8,
            key = "s8",
            title = "III.A.6 Patient’s / mother’s obstetric history",
            pageRange = "Pages 17–19",
            requiredPages = 3,
            hint = "Antenatal & obstetric background · Labour, delivery & neonatal · Particulars of each pregnancy",
            pages = listOf(17, 18, 19)
        ),
        ScrSection(
            index = 9,
            key = "s9",
            title = "III.A.7 Developmental landmarks & problems",
            pageRange = "Pages 19–21",
            requiredPages = 3,
            hint = "Physical & mental milestones · Behaviour & behavioural problems · Parental attitudes",
            pages = listOf(19, 20, 21)
        ),
        ScrSection(
            index = 10,
            key = "s10",
            title = "III.A.8 Diet & daily routine",
            pageRange = "Page 22",
            requiredPages = 1,
            hint = "Daily routine schedule · Meal patterns · Hydration · Sleep-wake cycles · Physical activity",
            pages = listOf(22)
        ),
        ScrSection(
            index = 11,
            key = "s11",
            title = "III.B Mental state & I.P.R.",
            pageRange = "Pages 23–24",
            requiredPages = 2,
            hint = "Structural model of mental state · Inter-Personal Relations (I.P.R.) Grid",
            pages = listOf(23, 24)
        ),
        ScrSection(
            index = 12,
            key = "s12",
            title = "Life-space investigation",
            pageRange = "Pages 25–30",
            requiredPages = 6,
            hint = "Society · Family · Inter-personal · Work · Life situations & dissatisfaction",
            pages = listOf(25, 26, 27, 28, 29, 30)
        ),
        ScrSection(
            index = 13,
            key = "s13",
            title = "Detailed mental totality, sleep & dreams",
            pageRange = "Pages 31–43",
            requiredPages = 13,
            hint = "Emotional state (Anger/Sadness/Love/Fear) · Intellectual state & consciousness · State effects & reactions · Sleep & Dreams inventory",
            pages = listOf(31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43)
        ),
        ScrSection(
            index = 14,
            key = "s14",
            title = "Diagnostic net & psychodynamic interpretation",
            pageRange = "Pages 44–46",
            requiredPages = 3,
            hint = "Diagnostic net matrix · Psychodynamic interpretation of mental state",
            pages = listOf(44, 45, 46)
        ),
        ScrSection(
            index = 15,
            key = "s15",
            title = "III.C Reactions – physical factors",
            pageRange = "Pages 47–54",
            requiredPages = 8,
            hint = "Time & periodicity · Motion & position · Meteorological & environmental · Sensory inputs · Physiological functions & food · Epochs of life",
            pages = listOf(47, 48, 49, 50, 51, 52, 53, 54)
        ),
        ScrSection(
            index = 16,
            key = "s16",
            title = "III.D Fever totality",
            pageRange = "Pages 55–56",
            requiredPages = 2,
            hint = "Onset · Type · Course · Chill · Heat · Sweat · Apyrexia · Compound fevers",
            pages = listOf(55, 56)
        ),
        ScrSection(
            index = 17,
            key = "s17",
            title = "III.E Original unmodified picture",
            pageRange = "Page 57",
            requiredPages = 1,
            hint = "Natural course of illness before suppressive medications or therapies",
            pages = listOf(57)
        ),
        ScrSection(
            index = 18,
            key = "s18",
            title = "III.F Chronological sequence",
            pageRange = "Page 58",
            requiredPages = 1,
            hint = "Timeline of ailments from birth to present presentation",
            pages = listOf(58)
        ),
        ScrSection(
            index = 19,
            key = "s19",
            title = "Past history (personal / family) & check-list",
            pageRange = "Pages 59–63",
            requiredPages = 5,
            hint = "Systematic pathology check-list · Systemic, pediatric, cardiorespiratory, hepatorenal, neurological, skin, allergies, vaccines",
            pages = listOf(59, 60, 61, 62, 63)
        ),
        ScrSection(
            index = 20,
            key = "s20",
            title = "Physical & systemic examination",
            pageRange = "Pages 64–66",
            requiredPages = 3,
            hint = "General physical examination · Anatomical regions · Respiratory · Abdomen · CVS · CNS",
            pages = listOf(64, 65, 66)
        ),
        ScrSection(
            index = 21,
            key = "s21",
            title = "Provisional clinical & differential diagnosis",
            pageRange = "Page 67",
            requiredPages = 1,
            hint = "Clinical diagnosis · Differential diagnosis rationale",
            pages = listOf(67)
        ),
        ScrSection(
            index = 22,
            key = "s22",
            title = "Investigations",
            pageRange = "Pages 68–73",
            requiredPages = 6,
            hint = "Urine & stool analysis · Haematology & serology · Imaging & specialized tests",
            pages = listOf(68, 69, 70, 71, 72, 73)
        ),
        ScrSection(
            index = 23,
            key = "s23",
            title = "Evolutionary Hahnemannian totality & miasmatic expression",
            pageRange = "Pages 74–85",
            requiredPages = 12,
            hint = "Evolutionary totality model · Miasmatic reference matrix · Essential totality diagrams",
            pages = listOf(74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85)
        ),
        ScrSection(
            index = 24,
            key = "s24",
            title = "Repertorial totality & differential field",
            pageRange = "Pages 86–89",
            requiredPages = 4,
            hint = "Repertorial strategy · Repertorial totality table · Thermal analysis · Potential differential field",
            pages = listOf(86, 87, 88, 89)
        ),
        ScrSection(
            index = 25,
            key = "s25",
            title = "Remedy-correspondences & selection",
            pageRange = "Pages 90–93",
            requiredPages = 4,
            hint = "Constitutional, acute, intercurrent totalities · Grounds of final remedy selection",
            pages = listOf(90, 91, 92, 93)
        ),
        ScrSection(
            index = 26,
            key = "s26",
            title = "Planning & programming of Rx",
            pageRange = "Pages 94–97",
            requiredPages = 4,
            hint = "Susceptibility · Sensitivity · Suppressions · Vitality · Miasm · Posology",
            pages = listOf(94, 95, 96, 97)
        ),
        ScrSection(
            index = 27,
            key = "s27",
            title = "Prescription & management",
            pageRange = "Pages 98–99",
            requiredPages = 2,
            hint = "Entry prescription log · General management & education",
            pages = listOf(98, 99)
        ),
        ScrSection(
            index = 28,
            key = "s28",
            title = "Therapeutic problem definition & resolution",
            pageRange = "Pages 100–101",
            requiredPages = 2,
            hint = "Therapeutic problems · Clinical goals & resolution",
            pages = listOf(100, 101)
        ),
        ScrSection(
            index = 29,
            key = "s29",
            title = "Progress-record: changes expected c Rx",
            pageRange = "Pages 102–103",
            requiredPages = 2,
            hint = "Expected response timeline · Evaluating direction of cure",
            pages = listOf(102, 103)
        ),
        ScrSection(
            index = 30,
            key = "s30",
            title = "Progress notes (follow-up log)",
            pageRange = "Pages 104–106",
            requiredPages = 3,
            hint = "Date · Symptom-changes (1-10) · Interpretation (I) / Expectation (E) · Prescription",
            pages = listOf(104, 105, 106)
        )
    )
}
