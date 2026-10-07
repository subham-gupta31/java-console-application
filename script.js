const storageKey = 'stayease-demo-data';

const roomCatalog = {
  SINGLE: { label: 'Single', price: 1500 },
  DOUBLE: { label: 'Double', price: 2500 },
  DELUXE: { label: 'Deluxe', price: 3500 },
  SUITE: { label: 'Suite', price: 5000 }
};

const roomBlueprints = [
  { roomNumber: 101, type: 'SINGLE' },
  { roomNumber: 102, type: 'SINGLE' },
  { roomNumber: 103, type: 'SINGLE' },
  { roomNumber: 201, type: 'DOUBLE' },
  { roomNumber: 202, type: 'DOUBLE' },
  { roomNumber: 203, type: 'DOUBLE' },
  { roomNumber: 301, type: 'DELUXE' },
  { roomNumber: 302, type: 'DELUXE' },
  { roomNumber: 401, type: 'SUITE' },
  { roomNumber: 402, type: 'SUITE' }
];

const defaultGuestList = [
  { guestId: 'G-1001', name: 'Aisha Khan', phone: '+971500111222', email: 'aisha@email.com' },
  { guestId: 'G-1002', name: 'Daniel Brooks', phone: '+971500333444', email: 'daniel@email.com' },
  { guestId: 'G-1003', name: 'Nurul Hassan', phone: '+971500555666', email: 'nurul@email.com' }
];

const state = {
  guests: [],
  rooms: [],
  reservations: []
};

const guestForm = document.getElementById('guestForm');
const reservationForm = document.getElementById('reservationForm');
const reservationGuest = document.getElementById('reservationGuest');
const reservationRoom = document.getElementById('reservationRoom');
const roomsContainer = document.getElementById('roomsContainer');
const reservationTableBody = document.getElementById('reservationTableBody');
const resetDataBtn = document.getElementById('resetDataBtn');

function initializeDemoData() {
  const saved = localStorage.getItem(storageKey);

  if (saved) {
    const parsed = JSON.parse(saved);
    state.guests = parsed.guests || [];
    state.rooms = parsed.rooms || [];
    state.reservations = parsed.reservations || [];
    return;
  }

  state.rooms = roomBlueprints.map((room) => ({
    roomNumber: room.roomNumber,
    type: room.type,
    available: true
  }));

  state.guests = defaultGuestList;
  state.reservations = [
    {
      reservationId: 'R-2001',
      guestId: 'G-1001',
      roomNumber: 101,
      checkInDate: '2026-10-04',
      checkOutDate: '2026-10-07',
      status: 'CHECKED_IN'
    },
    {
      reservationId: 'R-2002',
      guestId: 'G-1002',
      roomNumber: 201,
      checkInDate: '2026-10-06',
      checkOutDate: '2026-10-09',
      status: 'RESERVED'
    }
  ];

  syncRoomAvailability();
  saveState();
}

function saveState() {
  localStorage.setItem(storageKey, JSON.stringify({
    guests: state.guests,
    rooms: state.rooms,
    reservations: state.reservations
  }));
}

function syncRoomAvailability() {
  state.rooms.forEach((room) => {
    room.available = !state.reservations.some((reservation) => {
      return reservation.roomNumber === room.roomNumber &&
        ['RESERVED', 'CHECKED_IN'].includes(reservation.status);
    });
  });
}

function getGuestById(guestId) {
  return state.guests.find((guest) => guest.guestId === guestId) || null;
}

function getRoomByNumber(roomNumber) {
  return state.rooms.find((room) => room.roomNumber === Number(roomNumber)) || null;
}

function getReservationById(reservationId) {
  return state.reservations.find((reservation) => reservation.reservationId === reservationId) || null;
}

function calculateNights(checkInDate, checkOutDate) {
  const start = new Date(checkInDate);
  const end = new Date(checkOutDate);
  const diffMs = end - start;
  return Math.max(1, Math.round(diffMs / (1000 * 60 * 60 * 24)));
}

function calculateAmount(roomNumber, checkInDate, checkOutDate) {
  const room = getRoomByNumber(roomNumber);
  if (!room) return 0;
  const price = roomCatalog[room.type].price;
  return calculateNights(checkInDate, checkOutDate) * price;
}

function generateGuestId() {
  const nextId = state.guests.length + 1001;
  return `G-${nextId}`;
}

function generateReservationId() {
  const nextId = state.reservations.length + 2001;
  return `R-${nextId}`;
}

function renderSummary() {
  document.getElementById('totalGuests').textContent = state.guests.length;
  const availableRooms = state.rooms.filter((room) => room.available).length;
  const occupiedRooms = state.rooms.length - availableRooms;
  document.getElementById('availableRoomsCount').textContent = availableRooms;
  document.getElementById('occupiedRoomsCount').textContent = occupiedRooms;
  document.getElementById('totalReservations').textContent = state.reservations.length;
}

function renderGuestOptions() {
  const guestOptions = state.guests.map((guest) => {
    return `<option value="${guest.guestId}">${guest.name} (${guest.guestId})</option>`;
  }).join('');

  reservationGuest.innerHTML = '<option value="">Select a guest</option>' + guestOptions;
}

function renderRoomOptions() {
  const roomOptions = state.rooms.map((room) => {
    const status = room.available ? 'Available' : 'Occupied';
    return `<option value="${room.roomNumber}">Room ${room.roomNumber} - ${roomCatalog[room.type].label} (${status})</option>`;
  }).join('');

  reservationRoom.innerHTML = '<option value="">Select a room</option>' + roomOptions;
}

function renderRoomCards() {
  roomsContainer.innerHTML = state.rooms.map((room) => {
    const typeInfo = roomCatalog[room.type];
    const statusClass = room.available ? 'available' : 'occupied';
    const statusText = room.available ? 'Available' : 'Occupied';

    return `
      <article class="room-card">
        <div class="room-header">
          <div class="room-number">Room ${room.roomNumber}</div>
          <span class="status-pill ${statusClass}">${statusText}</span>
        </div>
        <div class="room-meta">
          <span><strong>Type:</strong> ${typeInfo.label}</span>
          <span><strong>Price:</strong> ₹${typeInfo.price.toLocaleString()}/night</span>
        </div>
      </article>
    `;
  }).join('');
}

function renderReservations() {
  if (!state.reservations.length) {
    reservationTableBody.innerHTML = '<tr><td colspan="8" class="empty-state">No reservations yet.</td></tr>';
    return;
  }

  reservationTableBody.innerHTML = state.reservations.map((reservation) => {
    const guest = getGuestById(reservation.guestId);
    const room = getRoomByNumber(reservation.roomNumber);
    const statusClass = reservation.status.toLowerCase().replace('_', '-');
    const price = calculateAmount(reservation.roomNumber, reservation.checkInDate, reservation.checkOutDate);
    const nights = calculateNights(reservation.checkInDate, reservation.checkOutDate);

    const actions = [];
    if (reservation.status === 'RESERVED') {
      actions.push('<button class="action-btn check-in" data-action="check-in" data-id="'+reservation.reservationId+'">Check In</button>');
    }
    if (reservation.status === 'CHECKED_IN') {
      actions.push('<button class="action-btn check-out" data-action="check-out" data-id="'+reservation.reservationId+'">Check Out</button>');
    }
    if (['RESERVED', 'CHECKED_IN'].includes(reservation.status)) {
      actions.push('<button class="action-btn cancel" data-action="cancel" data-id="'+reservation.reservationId+'">Cancel</button>');
    }

    return `
      <tr>
        <td>${reservation.reservationId}</td>
        <td>${guest ? guest.name : 'Unknown Guest'}</td>
        <td>Room ${reservation.roomNumber} (${room ? roomCatalog[room.type].label : 'Unknown'})</td>
        <td>${reservation.checkInDate} to ${reservation.checkOutDate}</td>
        <td>${nights}</td>
        <td>₹${price.toLocaleString()}</td>
        <td><span class="badge ${statusClass}">${reservation.status.replace('_', ' ')}</span></td>
        <td><div class="actions">${actions.join('')}</div></td>
      </tr>
    `;
  }).join('');
}

function renderAll() {
  syncRoomAvailability();
  renderSummary();
  renderGuestOptions();
  renderRoomOptions();
  renderRoomCards();
  renderReservations();
  saveState();
}

function handleGuestSubmit(event) {
  event.preventDefault();

  const name = document.getElementById('guestName').value.trim();
  const phone = document.getElementById('guestPhone').value.trim();
  const email = document.getElementById('guestEmail').value.trim();

  if (!name || !phone || !email) {
    alert('Please fill in all guest details.');
    return;
  }

  state.guests.push({
    guestId: generateGuestId(),
    name,
    phone,
    email
  });

  guestForm.reset();
  renderAll();
}

function handleReservationSubmit(event) {
  event.preventDefault();

  const guestId = reservationGuest.value;
  const roomNumber = Number(reservationRoom.value);
  const checkInDate = document.getElementById('checkInDate').value;
  const checkOutDate = document.getElementById('checkOutDate').value;

  if (!guestId || !roomNumber || !checkInDate || !checkOutDate) {
    alert('Please complete all reservation fields.');
    return;
  }

  if (new Date(checkOutDate) <= new Date(checkInDate)) {
    alert('Check-out date must be after the check-in date.');
    return;
  }

  const guest = getGuestById(guestId);
  const room = getRoomByNumber(roomNumber);

  if (!guest) {
    alert('Guest not found.');
    return;
  }

  if (!room) {
    alert('Room not found.');
    return;
  }

  const roomUnavailable = state.reservations.some((reservation) => {
    return reservation.roomNumber === roomNumber && ['RESERVED', 'CHECKED_IN'].includes(reservation.status);
  });

  if (!room.available || roomUnavailable) {
    alert('This room is not available for the chosen dates.');
    return;
  }

  state.reservations.push({
    reservationId: generateReservationId(),
    guestId,
    roomNumber,
    checkInDate,
    checkOutDate,
    status: 'RESERVED'
  });

  reservationForm.reset();
  renderAll();
}

function handleReservationAction(event) {
  const button = event.target.closest('[data-action]');
  if (!button) return;

  const reservationId = button.dataset.id;
  const action = button.dataset.action;
  const reservation = getReservationById(reservationId);

  if (!reservation) return;

  if (action === 'check-in') {
    reservation.status = 'CHECKED_IN';
  }

  if (action === 'check-out') {
    if (reservation.status !== 'CHECKED_IN') {
      alert('Guest must check in before check-out.');
      return;
    }

    reservation.status = 'CHECKED_OUT';
  }

  if (action === 'cancel') {
    if (['CANCELLED', 'CHECKED_OUT'].includes(reservation.status)) {
      alert('This reservation cannot be cancelled.');
      return;
    }
    reservation.status = 'CANCELLED';
  }

  renderAll();
}

function resetDemoData() {
  localStorage.removeItem(storageKey);
  initializeDemoData();
  renderAll();
}

guestForm.addEventListener('submit', handleGuestSubmit);
reservationForm.addEventListener('submit', handleReservationSubmit);
reservationTableBody.addEventListener('click', handleReservationAction);
resetDataBtn.addEventListener('click', resetDemoData);

initializeDemoData();
renderAll();
