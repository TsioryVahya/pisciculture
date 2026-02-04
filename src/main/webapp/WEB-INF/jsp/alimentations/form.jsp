<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="fr" class="h-full bg-white">
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="h-full overflow-hidden flex">
    <jsp:include page="/WEB-INF/jsp/components/sidebar.jsp" />

    <div class="flex-1 flex flex-col min-w-0 overflow-hidden">
        <header class="bg-white border-b border-gray-200">
            <div class="px-4 sm:px-6 lg:px-8">
                <div class="flex justify-between h-16 items-center">
                    <h1 class="text-lg font-semibold text-gray-900">${title}</h1>
                </div>
            </div>
        </header>

        <main class="flex-1 overflow-y-auto bg-gray-50 p-6">
            <div class="max-w-3xl mx-auto">
                <div class="bg-white shadow sm:rounded-lg p-6 border border-gray-200">
                    <form action="${pageContext.request.contextPath}/alimentations/save" method="POST">
                        <div class="space-y-6">
                            <!-- Étang Selection -->
                            <div>
                                <label for="etangId" class="block text-sm font-medium text-gray-700">Sélectionner l'Étang</label>
                                <select id="etangId" name="etangId" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                                    <option value="">-- Choisir un étang --</option>
                                    <c:forEach items="${etangs}" var="e">
                                        <option value="${e.id}">Étang #${e.id} (Surface: ${e.surface} m²)</option>
                                    </c:forEach>
                                </select>
                            </div>

                            <!-- Nourriture List -->
                            <div class="border-t border-gray-200 pt-6">
                                <h3 class="text-lg font-medium text-gray-900">Nourritures et Quantités</h3>
                                <p class="mt-1 text-sm text-gray-500">Ajoutez les nourritures utilisées pour cette alimentation.</p>

                                <div id="nourritureRows" class="space-y-4 mt-4">
                                    <div class="nourriture-row grid grid-cols-1 gap-y-2 gap-x-4 sm:grid-cols-12 items-end">
                                        <div class="sm:col-span-7">
                                            <label class="block text-xs font-medium text-gray-500 uppercase">Nourriture</label>
                                            <select name="nourritureIds" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm">
                                                <option value="">-- Sélectionner --</option>
                                                <c:forEach items="${nourritures}" var="n">
                                                    <option value="${n.id}">${n.nom} (${n.prixAchatParKg} Ar/kg)</option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="sm:col-span-4">
                                            <label class="block text-xs font-medium text-gray-500 uppercase">Quantité (kg)</label>
                                            <input type="number" step="0.01" min="0.01" name="quantites" required class="mt-1 block w-full border border-gray-300 rounded-md shadow-sm py-2 px-3 focus:outline-none focus:ring-emerald-500 focus:border-emerald-500 sm:text-sm" placeholder="0.00">
                                        </div>
                                        <div class="sm:col-span-1 flex justify-end">
                                            <button type="button" class="remove-row text-red-600 hover:text-red-900 focus:outline-none p-2" title="Supprimer">
                                                <svg class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>

                                <button type="button" id="addRow" class="mt-4 inline-flex items-center px-3 py-1.5 border border-gray-300 shadow-sm text-xs font-medium rounded text-gray-700 bg-white hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                    <svg class="-ml-0.5 mr-2 h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 4v16m8-8H4" />
                                    </svg>
                                    Ajouter une ligne
                                </button>
                            </div>

                            <div class="pt-5 flex justify-end space-x-3">
                                <a href="${pageContext.request.contextPath}/alimentations" class="bg-white py-2 px-4 border border-gray-300 rounded-md shadow-sm text-sm font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                    Annuler
                                </a>
                                <button type="submit" class="inline-flex justify-center py-2 px-4 border border-transparent shadow-sm text-sm font-medium rounded-md text-white bg-emerald-600 hover:bg-emerald-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-emerald-500">
                                    Enregistrer l'Alimentation
                                </button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </main>
    </div>

    <script>
        document.getElementById('addRow').addEventListener('click', function() {
            const container = document.getElementById('nourritureRows');
            const firstRow = container.querySelector('.nourriture-row');
            const newRow = firstRow.cloneNode(true);
            
            // Clear inputs
            newRow.querySelector('select').value = '';
            newRow.querySelector('input').value = '';
            
            container.appendChild(newRow);
            
            // Add remove event to the new row
            newRow.querySelector('.remove-row').addEventListener('click', function() {
                if (container.querySelectorAll('.nourriture-row').length > 1) {
                    newRow.remove();
                }
            });
        });

        // Add remove event to the initial row
        document.querySelectorAll('.remove-row').forEach(button => {
            button.addEventListener('click', function() {
                const container = document.getElementById('nourritureRows');
                if (container.querySelectorAll('.nourriture-row').length > 1) {
                    button.closest('.nourriture-row').remove();
                }
            });
        });
    </script>
</body>
</html>
