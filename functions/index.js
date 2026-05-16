const functions = require("firebase-functions");
const admin = require("firebase-admin");

admin.initializeApp();

// Notify host when a guest sends an inquiry
exports.onNewInquiry = functions.firestore
    .document("inquiries/{inquiryId}")
    .onCreate(async (snap, context) => {
        const inquiry = snap.data();
        const hostId = inquiry.hostId;
        const guestName = inquiry.guestName || "A guest";

        // Get host's FCM token from their user doc
        const hostDoc = await admin.firestore().collection("users").doc(hostId).get();
        const token = hostDoc.data()?.fcmToken;
        if (!token) return;

        const message = {
            token: token,
            notification: {
                title: "New Inquiry",
                body: `${guestName} is interested in your property`,
            },
            data: {
                screen: "inquiries",
                homestayId: inquiry.homestayId || "",
                inquiryId: context.params.inquiryId,
            },
        };

        try {
            await admin.messaging().send(message);
        } catch (error) {
            functions.logger.error("Failed to send inquiry notification", error);
        }
    });

// Notify host when a guest makes a booking
exports.onNewBooking = functions.firestore
    .document("bookings/{bookingId}")
    .onCreate(async (snap, context) => {
        const booking = snap.data();
        const hostId = booking.hostId;

        const hostDoc = await admin.firestore().collection("users").doc(hostId).get();
        const token = hostDoc.data()?.fcmToken;
        if (!token) return;

        const message = {
            token: token,
            notification: {
                title: "New Booking",
                body: `Your property has been booked for ${booking.checkIn || "selected dates"}`,
            },
            data: {
                screen: "bookings",
                homestayId: booking.homestayId || "",
                bookingId: context.params.bookingId,
            },
        };

        try {
            await admin.messaging().send(message);
        } catch (error) {
            functions.logger.error("Failed to send booking notification", error);
        }
    });
