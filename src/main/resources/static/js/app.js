const API_URL = '';

function getToken() {
  return localStorage.getItem('token');
}

function logout() {
  localStorage.clear();
  window.location.href = 'login.html';
}

// Cargar nombre de usuario y citas al iniciar
document.addEventListener('DOMContentLoaded', () => {
  const username = localStorage.getItem('username') || 'Usuario';
  const elem = document.getElementById('nombreUsuario');
  if (elem) elem.textContent = username;

  if (window.location.pathname.includes('citas.html') && getToken()) {
    cargarCitas();
  }
});

let citaIdEditar = null;

// ==================== REGISTRAR O MODIFICAR CITA ====================
async function guardarCita() {
  const cita = {
    especialidad: document.getElementById('especialidad').value,
    fecha: document.getElementById('fecha').value + ":00",
    motivo: document.getElementById('motivo').value.trim() || null
  };

  const url = citaIdEditar ? `/api/citas/${citaIdEditar}` : '/api/citas';
  const method = citaIdEditar ? 'PUT' : 'POST';

  try {
    const res = await fetch(url, {
      method: method,
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getToken()
      },
      body: JSON.stringify(cita)
    });

    if (res.ok) {
      alert(citaIdEditar ? 'Cita modificada correctamente' : 'Cita agendada correctamente');
      bootstrap.Modal.getInstance(document.getElementById('nuevaCitaModal')).hide();
      cargarCitas();
    } else {
      const error = await res.text();
      alert('Error: ' + error);
    }
  } catch (err) {
    alert('Error de conexión');
  }
}

// Abrir modal para editar
function abrirEditar(id, especialidad, fecha, motivo) {
  citaIdEditar = id;
  document.getElementById('especialidad').value = especialidad;
  document.getElementById('fecha').value = fecha;
  document.getElementById('motivo').value = motivo || '';
  document.querySelector('#nuevaCitaModal .modal-title').textContent = 'Modificar Cita';
  document.getElementById('btnGuardarCita').textContent = 'Guardar Cambios';
  bootstrap.Modal.getOrCreateInstance(document.getElementById('nuevaCitaModal')).show();
}

// Resetear modal al cerrar
document.getElementById('nuevaCitaModal')?.addEventListener('hidden.bs.modal', () => {
  document.querySelector('#nuevaCitaModal .modal-title').textContent = 'Agendar nueva cita';
  document.getElementById('btnGuardarCita').textContent = 'Agendar';
  document.getElementById('btnGuardarCita').onclick = guardarCita;
  document.getElementById('formNuevaCita').reset();
  citaIdEditar = null;
});

// ==================== LISTAR CITAS ====================
async function cargarCitas() {
  try {
    const res = await fetch('/api/citas/mis', {
      headers: { 'Authorization': 'Bearer ' + getToken() }
    });

    if (!res.ok) {
      if (res.status === 401) logout();
      return;
    }

    const citas = await res.json();
    const cont = document.getElementById('listaCitas');

    if (citas.length === 0) {
      cont.innerHTML = '<div class="alert alert-info text-center">No tienes citas agendadas.</div>';
      return;
    }

    let html = '<div class="row row-cols-1 row-cols-md-2 g-4">';
    citas.forEach(c => {
      const fechaISO = c.fecha.substring(0, 16);
      const esModificable = c.estado === 'PROGRAMADA' || c.estado === 'PENDIENTE';
      const badgeClass = esModificable ? 'warning' : c.estado === 'CANCELADA' ? 'danger' : 'success';

      // Escapamos comillas para evitar errores JS
      const motivoJS = (c.motivo || '').replace(/'/g, "\\'").replace(/"/g, '&quot;');

      html += `
        <div class="col">
          <div class="card h-100 shadow-sm">
            <div class="card-body">
              <h5 class="card-title text-primary">${c.especialidad}</h5>
              <p class="mb-1"><strong>Fecha:</strong> ${new Date(c.fecha).toLocaleString('es-PE')}</p>
              <p class="mb-2"><strong>Motivo:</strong> ${c.motivo || '—'}</p>
              <span class="badge bg-${badgeClass}">${c.estado}</span>
              
              <div class="mt-3">
                ${esModificable ? `
                  <button class="btn btn-sm btn-outline-primary me-2" 
                          onclick="abrirEditar(${c.id}, '${c.especialidad}', '${fechaISO}', '${motivoJS}')">
                    Modificar
                  </button>
                  <button class="btn btn-sm btn-outline-danger" onclick="cancelarCita(${c.id})">
                    Cancelar
                  </button>
                ` : '<small class="text-muted">No se puede modificar</small>'}
              </div>
            </div>
          </div>
        </div>`;
    });
    html += '</div>';
    cont.innerHTML = html;

  } catch (err) {
    console.error(err);
    document.getElementById('listaCitas').innerHTML = '<div class="alert alert-danger">Error al cargar citas</div>';
  }
}

// ==================== CANCELAR CITA ====================
async function cancelarCita(id) {
  if (!confirm('¿Seguro que deseas cancelar esta cita?')) return;

  const res = await fetch(`/api/citas/${id}`, {
    method: 'DELETE',
    headers: { 'Authorization': 'Bearer ' + getToken() }
  });

  if (res.ok) {
    alert('Cita cancelada correctamente');
    cargarCitas();
  } else {
    alert('Error al cancelar');
  }
}